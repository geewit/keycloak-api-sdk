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

import org.keycloak.representations.idm.RoleRepresentation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author geewit
 */
public interface RolesResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<RoleRepresentation> list();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    void create(RoleRepresentation roleRepresentation);

    @Path("{roleName}")
    RoleResource get(@PathParam("roleName") String roleName);

    @Path("{role-name}")
    @DELETE
    void deleteRole(final @PathParam("role-name") String roleName);

}
