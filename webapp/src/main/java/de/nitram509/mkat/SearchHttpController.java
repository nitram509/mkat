package de.nitram509.mkat;

import de.nitram509.mkat.api.search.SearchField;
import de.nitram509.mkat.api.search.SearchOptions;
import de.nitram509.mkat.api.search.SearchResult;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/search")
public class SearchHttpController {

  @Inject
  SearchService searchService;

  @Inject
  DiskContentService diskContentService;

  @GET
  @Path("{query}")
  @Produces({APPLICATION_JSON})
  public Response doSearch(@PathParam("query") String query,
                           @QueryParam("kw") String keywordCombination,
                           @QueryParam("order") String order,
                           @QueryParam("fields") String fields) {
    long startTime = System.currentTimeMillis();
    SearchOptions searchOptions = new SearchOptions();
    searchOptions.setKeywordCombination(keywordCombination);
    searchOptions.setQuery(query);
    searchOptions.setOrderByField(order);
    boolean addDefaultFields = true;
    if (fields != null) {
      for (String fieldName : fields.split(",")) {
        searchOptions.addSearchField(SearchField.valueFrom(fieldName, null));
        addDefaultFields = false;
      }
    }
    if (addDefaultFields) searchOptions.addDefaultSearchFields();
    SearchResult searchResult = searchService.search(searchOptions);
    long endTime = System.currentTimeMillis();
    searchResult.setQueryTime(endTime - startTime);
    searchResult.setQuery(query);
    return Response.ok(searchResult).build();
  }

}
