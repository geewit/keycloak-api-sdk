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

package io.geewit.keycloak.client.token;

import org.keycloak.representations.AccessTokenResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

/**
 * @author rodrigo.sasaki@icarros.com.br
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Path("/realms/{realm}/protocol/openid-connect/token")
public interface TokenService {

    @POST
    AccessTokenResponse grantToken(@PathParam("realm") String realm, MultivaluedMap<String, String> map);

    @POST
    AccessTokenResponse refreshToken(@PathParam("realm") String realm, MultivaluedMap<String, String> map);

}
