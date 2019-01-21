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

import org.keycloak.representations.idm.ClientScopeRepresentation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author geewit
 */
public interface ClientScopeResource {

    @Path("protocol-mappers")
    ProtocolMappersResource getProtocolMappers();

    @Path("/scope-mappings")
    RoleMappingResource getScopeMappings();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    ClientScopeRepresentation toRepresentation();

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    void update(ClientScopeRepresentation rep);

    @DELETE
    void remove();


}