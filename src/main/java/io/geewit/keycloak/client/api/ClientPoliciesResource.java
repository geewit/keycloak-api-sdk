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
import org.keycloak.representations.idm.authorization.ClientPolicyRepresentation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author geewit
 */
public interface ClientPoliciesResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response create(ClientPolicyRepresentation representation);

    @Path("{id}")
    ClientPolicyResource findById(@PathParam("id") String id);

    @Path("/search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @NoCache
    ClientPolicyRepresentation findByName(@QueryParam("name") String name);
}
