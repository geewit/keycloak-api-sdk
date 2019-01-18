package io.geewit.keycloak.client;

import io.geewit.keycloak.client.api.UsersResource;
import io.geewit.keycloak.client.okhttp.OkHttpClients;
import io.geewit.keycloak.client.resteasy.engine.OkHttpClientEngine;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.Test;

import javax.ws.rs.core.UriBuilder;

import static org.keycloak.OAuth2Constants.CLIENT_CREDENTIALS;
import static org.keycloak.OAuth2Constants.PASSWORD;

public class KeycloakClientTest {

    @Test
    public void testUsersResource() {
        System.out.println("run keycloak client test");
        String baseUrl = "http://127.0.0.1:8080/auth";
        String realm = "app-authz-springboot";
        String clientId = "app-authz-springboot";
        String clientSecret = "766ef9ee-240f-4faa-8602-2805cd0213a4";
        String username = "admin";
        String password = "admin123456";
        Keycloak keycloak = KeycloakBuilder.builder().serverUrl(baseUrl).clientId(clientId).clientSecret(clientSecret).username(username).password(password).grantType(PASSWORD).realm(realm).build();
        UsersResource proxy = keycloak.proxy(UsersResource.class);
        System.out.println("total users = " + proxy.count(realm));
    }
}
