package io.geewit.keycloak.client;

import com.google.common.collect.Lists;
import io.geewit.keycloak.client.api.UserResource;
import io.geewit.keycloak.client.api.UsersResource;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;


import java.util.List;

import static org.keycloak.OAuth2Constants.PASSWORD;

public class KeycloakClientTest {

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
    public void testUsersResource() {
        System.out.println("run keycloak client test");

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
