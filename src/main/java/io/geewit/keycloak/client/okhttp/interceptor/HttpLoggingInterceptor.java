package io.geewit.keycloak.client.okhttp.interceptor;

import okhttp3.*;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author geewit
 */
@SuppressWarnings({"unused"})
public class HttpLoggingInterceptor implements Interceptor {
    private final static Logger logger = LoggerFactory.getLogger(HttpLoggingInterceptor.class);

    public enum Level {
        /** No logs. */
        NONE,
        /**
         * Logs request and response lines.
         *
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1 (3-byte body)
         *
         * <-- 200 OK (22ms, 6-byte body)
         * }</pre>
         */
        BASIC,
        /**
         * Logs request and response lines and their respective headers.
         *
         * Example:
         * <pre>{@code
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         * <-- END HTTP
         * }</pre>
         *
         */
        HEADERS,
        /**
         * Logs request and response lines and their respective headers and bodies (if present).
         *
         * Example:
         * <pre>{@code
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         *
         * Hi?
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         *
         * Hello!
         * <-- END HTTP
         * }</pre>
         */
        BODY
    }

    private volatile Level level;

    /**
     * Change the level at which this interceptor logs.
     * @param level 日志级别
     * */
    public HttpLoggingInterceptor(Level level) {
        this.level = level == null ? Level.NONE : level;
    }

    public Level getLevel() {
        return level;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Level level = this.level;

        Request request = chain.request();
        if (level == Level.NONE) {
            return chain.proceed(request);
        }

        boolean logBody = level == Level.BODY;
        boolean logHeaders = logBody || level == Level.HEADERS;

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        Connection connection = chain.connection();
        String requestStartMessage = "--> " + request.method() + ' ' + request.url() + (connection != null ? " " + connection.protocol() : "");
        if (!logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
        }
        logger.debug(requestStartMessage);

        if (logHeaders) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody.contentType() != null) {
                    logger.debug("Content-Type: " + requestBody.contentType());
                }
                if (requestBody.contentLength() != -1) {
                    logger.debug("Content-Length: " + requestBody.contentLength());
                }
            }

            Headers headers = request.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                String name = headers.name(i);
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                    logger.debug(name + ": " + headers.value(i));
                }
            }

            if (!logBody || !hasRequestBody) {
                logger.debug("--> END " + request.method());
            } else if (bodyEncoded(request.headers())) {
                logger.debug("--> END " + request.method() + " (encoded body omitted)");
            } else {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                Charset charset = StandardCharsets.UTF_8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    contentType.charset(StandardCharsets.UTF_8);
                }

                if (isPlaintext(buffer)) {
                    logger.debug("requestBody" + buffer.readString(charset));
                    logger.debug("--> END " + request.method() + " (" + requestBody.contentLength() + "-byte body)");
                } else {
                    logger.debug("--> END " + request.method() + " (binary " + requestBody.contentLength() + "-byte body omitted)");
                }
            }
        }

        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            logger.warn("<-- HTTP FAILED: " + e.getMessage(), e);
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        if(responseBody == null) {
            throw new IOException("responseBody == null");
        }
        long contentLength = responseBody.contentLength();
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        logger.debug("<-- "
                + response.code()
                + (response.message().isEmpty() ? "" : ' ' + response.message())
                + ' ' + response.request().url()
                + " (" + tookMs + "ms" + (!logHeaders ? ", " + bodySize + " body" : "") + ')');

        if (logHeaders) {
            Headers headers = response.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                logger.debug(headers.name(i) + ": " + headers.value(i));
            }

            if (!logBody || !HttpHeaders.hasBody(response)) {
                logger.debug("<-- END HTTP");
            } else if (bodyEncoded(response.headers())) {
                logger.debug("<-- END HTTP (encoded body omitted)");
            } else {
                BufferedSource source = responseBody.source();
                // Buffer the entire body.
                source.request(Long.MAX_VALUE);
                Buffer buffer = source.buffer();

                Charset charset = StandardCharsets.UTF_8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(StandardCharsets.UTF_8);
                }

                if (!isPlaintext(buffer)) {
                    logger.debug("");
                    logger.debug("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)");
                    return response;
                }

                if (contentLength != 0) {
                    logger.debug("");
                    assert charset != null;
                    logger.debug(buffer.clone().readString(charset));
                }

                logger.debug("<-- END HTTP (" + buffer.size() + "-byte body)");
            }
        }

        return response;
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            // Truncated UTF-8 sequence.
            return false;
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }
}