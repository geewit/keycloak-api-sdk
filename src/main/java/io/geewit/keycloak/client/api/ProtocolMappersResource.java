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

import org.keycloak.representations.idm.ProtocolMapperRepresentation;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author geewit
 */
public interface ProtocolMappersResource {

    @GET
    @Path("protocol/{protocol}")
    @Produces("application/json")
    List<ProtocolMapperRepresentation> getMappersPerProtocol(@PathParam("protocol") String protocol);

    @Path("models")
    @POST
    @Consumes("application/json")
    Response createMapper(ProtocolMapperRepresentation rep);

    @Path("add-models")
    @POST
    @Consumes("application/json")
    void createMapper(List<ProtocolMapperRepresentation> reps);

    @GET
    @Path("models")
    @Produces("application/json")
    List<ProtocolMapperRepresentation> getMappers();

    @GET
    @Path("models/{id}")
    @Produces("application/json")
    ProtocolMapperRepresentation getMapperById(@PathParam("id") String id);

    @PUT
    @Path("models/{id}")
    @Consumes("application/json")
    void update(@PathParam("id") String id, ProtocolMapperRepresentation rep);

    @DELETE
    @Path("models/{id}")
    void delete(@PathParam("id") String id);
}
