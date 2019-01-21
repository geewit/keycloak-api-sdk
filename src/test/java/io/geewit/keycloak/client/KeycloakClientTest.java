package io.geewit.keycloak.client;

import com.google.common.collect.Lists;
import io.geewit.keycloak.client.api.UserResource;
import io.geewit.keycloak.client.api.UsersResource;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;

import static org.keycloak.OAuth2Constants.PASSWORD;

public class KeycloakClientTest {
    private final static Logger logger = LoggerFactory.getLogger(KeycloakClientTest.class);


    private Keycloak keycloak;

    private String realm;

    @Before
    public void setUp() {
        String baseUrl = "http://localhost:8080/auth";
        String master = "master";
        String username = "admin";
        String password = "admin123456";
        realm = "app-authz-springboot";
        keycloak = KeycloakBuilder.builder()
                .serverUrl(baseUrl)
                .clientId("security-admin-console")
                .username(username).password(password)
                .grantType(PASSWORD)
                .realm(master)
                .build();
    }

    @Test
    public void testLogin() {
        logger.debug("run keycloak tokenManager client test");
        Assertions.assertThatCode(() -> keycloak.tokenManager().grantToken())
                .doesNotThrowAnyException();
    }


    @Test
    public void testUsersResource() {
        logger.debug("run keycloak UsersResource client test");

        UsersResource usersResource = keycloak.realm(realm).users();

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername("george");
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue("123456");
        credential.setTemporary(false);
        List<CredentialRepresentation> credentials = Lists.newArrayList(credential);
        userRepresentation.setCredentials(credentials);
        userRepresentation.setEnabled(true);
        usersResource.create(userRepresentation);


        Assertions.assertThatCode(() -> usersResource.search(null, null, null, null, 0, 20, true))
                .doesNotThrowAnyException();

        List<UserRepresentation> users = usersResource.search(null, null, null, null, 0, 20, true);



        Assertions.assertThatCode(usersResource::count)
                .doesNotThrowAnyException();
        int count = usersResource.count();

        if(count > 0) {
            String userId = users.get(0).getId();
            UserResource userResource = usersResource.get(userId);
            Assertions.assertThatCode(userResource::groups)
                    .doesNotThrowAnyException();
        }




    }
}
