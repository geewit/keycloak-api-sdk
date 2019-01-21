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

import org.jboss.resteasy.annotations.cache.NoCache;
import org.keycloak.representations.idm.authorization.ScopeRepresentation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author geewit
 */
public interface ResourceScopesResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response create(ScopeRepresentation scope);

    @Path("{id}")
    ResourceScopeResource scope(@PathParam("id") String id);

    @GET
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    List<ScopeRepresentation> scopes();

    @Path("/search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @NoCache
    ScopeRepresentation findByName(@QueryParam("name") String name);
}
