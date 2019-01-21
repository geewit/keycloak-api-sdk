/*
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
package io.geewit.keycloak.client.api;

import org.keycloak.representations.idm.authorization.ResourceServerRepresentation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author geewit
 */
public interface AuthorizationResource {

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void update(ResourceServerRepresentation server);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    ResourceServerRepresentation getSettings();

    @Path("/import")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    void importSettings(ResourceServerRepresentation server);

    @Path("/settings")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    ResourceServerRepresentation exportSettings();

    @Path("/api")
    ResourcesResource resources();

    @Path("/scope")
    ResourceScopesResource scopes();

    @Path("/policy")
    PoliciesResource policies();

    @Path("/permission")
    PermissionsResource permissions();
}
