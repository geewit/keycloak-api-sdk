package io.geewit.keycloak.client;

import io.geewit.keycloak.client.api.UsersResource;
import io.geewit.keycloak.client.okhttp.OkHttpClients;
import io.geewit.keycloak.client.resteasy.engine.OkHttpClientEngine;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.Test;

import javax.ws.rs.core.UriBuilder;

public class KeycloakClientTest {

    @Test
    public void testUsersResource() {
        System.out.println("run keycloak client test");
        String baseUrl = "http://127.0.0.1:8080/auth";
        ResteasyClient client = new ResteasyClientBuilder().httpEngine(new OkHttpClientEngine(OkHttpClients.buildClient(baseUrl))).build();
        ResteasyWebTarget target = client.target(UriBuilder.fromPath(baseUrl));
        UsersResource proxy = target.proxy(UsersResource.class);
        System.out.println("total users = " + proxy.count());
    }
}
