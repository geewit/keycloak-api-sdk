package io.geewit.keycloak.client.okhttp;

import io.geewit.keycloak.client.okhttp.interceptor.HttpLoggingInterceptor;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class OkHttpClients {

    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(HttpLoggingInterceptor.Level.BODY);


    public static OkHttpClient buildClient(String baseUrl) {
        if(StringUtils.startsWithIgnoreCase(baseUrl, "https")) {
            return httpsClient();
        } else {
            return httpClient();
        }
    }


    public static OkHttpClient buildClient(URI uri) {
        String scheme = uri.getScheme();
        return buildClient(scheme);
    }


    public static OkHttpClient httpClient() {
        return new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build();
    }

    public static OkHttpClient httpsClient() {
        return new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .hostnameVerifier((hostname, session) -> true)
                .addInterceptor(loggingInterceptor)
                .sslSocketFactory(Objects.requireNonNull(createSSLSocketFactory()), createTrustManager())
                .build();
    }

    private static X509TrustManager createTrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null,  new TrustManager[] { createTrustManager() }, new SecureRandom());
            return sc.getSocketFactory();
        } catch (KeyManagementException | NoSuchAlgorithmException ignored) {
            return null;
        }
    }

}
