/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.geewit.keycloak.client.builders;

import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;

import java.util.Collections;

/**
 * @author geewit
 */
public class ClientBuilder {

    private ClientRepresentation representation;

    public enum AccessType { BEARER_ONLY, PUBLIC, CONFIDENTIAL }

    public static ClientBuilder create(String clientId, String secret) {
        ClientRepresentation representation = new ClientRepresentation();
        representation.setEnabled(Boolean.TRUE);
        representation.setClientId(clientId);
        representation.setSecret(secret);
        return new ClientBuilder(representation);
    }

    private ClientBuilder(ClientRepresentation representation) {
        this.representation = representation;
    }

    public ClientRepresentation accessType(AccessType accessType) {
        switch (accessType) {
            case BEARER_ONLY:
                representation.setBearerOnly(true);
                break;
            case PUBLIC:
                representation.setPublicClient(true);
                break;
            case CONFIDENTIAL:
                representation.setPublicClient(false);
                break;
        }
        return defaultSettings();
    }

    public ClientBuilder rootUrl(String rootUrl) {
        representation.setRootUrl(rootUrl);
        return this;
    }

    public ClientBuilder redirectUri(String redirectUri) {
        representation.setRedirectUris(Collections.singletonList(redirectUri));
        return this;
    }

    public ClientBuilder baseUrl(String baseUrl) {
        representation.setBaseUrl(baseUrl);
        return this;
    }

    public ClientBuilder adminUrl(String adminUrl) {
        representation.setAdminUrl(adminUrl);
        return this;
    }

    public ClientBuilder secret(String secret) {
        representation.setSecret(secret);
        return this;
    }

    private ClientRepresentation defaultSettings() {
        representation.setFullScopeAllowed(true);
        representation.setDirectAccessGrantsEnabled(true);
        representation.setAdminUrl(representation.getRootUrl());

        if (representation.getRedirectUris() == null && representation.getRootUrl() != null)
            representation.setRedirectUris(Collections.singletonList(representation.getRootUrl().concat("/*")));
        return representation;
    }

}
