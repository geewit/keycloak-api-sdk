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
import org.keycloak.representations.idm.GroupRepresentation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

/**
 * @author geewit
 */
public interface GroupsResource {

    /**
     * Get all groups.
     * @return A list containing all groups.
     */
    @GET
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    List<GroupRepresentation> groups();

    /**
     * Get groups by pagination params.
     * @param first index of the first element
     * @param max max number of occurrences
     * @return A list containing the slice of all groups.
     */
    @GET
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    List<GroupRepresentation> groups(@QueryParam("first") Integer first, @QueryParam("max") Integer max);

    /**
     * Get groups by pagination params.
     * @param search max number of occurrences
     * @param first index of the first element
     * @param max max number of occurrences
     * @return A list containing the slice of all groups.
     */
    @GET
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    List<GroupRepresentation> groups(@QueryParam("search") String search,
                                     @QueryParam("first") Integer first,
                                     @QueryParam("max") Integer max);

    /**
     * Counts all groups.
     * @return A map containing key "count" with number of groups as value.
     */
    @GET
    @NoCache
    @Path("count")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Map<String, Long> count();

    /**
     * Counts groups by name search.
     * @param search max number of occurrences
     * @return A map containing key "count" with number of groups as value which matching with search.
     */
    @GET
    @NoCache
    @Path("count")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Map<String, Long> count(@QueryParam("search") String search);

    /**
     * Counts groups by name search.
     * @param onlyTopGroups <code>true</code> or <code>false</code> for filter only top level groups count
     * @return A map containing key "count" with number of top level groups.
     */
    @GET
    @NoCache
    @Path("count")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Map<String, Long> count(@QueryParam("top") @DefaultValue("true") boolean onlyTopGroups);

    /**
     * create or add a top level realm groupSet or create child.  This will update the group and set the parent if it exists.  Create it and set the parent
     * if the group doesn't exist.
     *
     * @param rep
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    Response add(GroupRepresentation rep);

    @Path("{id}")
    GroupResource group(@PathParam("id") String id);

}
