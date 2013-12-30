package de.nitram509.mkat;

import de.nitram509.mkat.api.diskcontent.DiskContent;
import de.nitram509.mkat.api.search.SearchField;
import de.nitram509.mkat.api.search.SearchOptions;
import de.nitram509.mkat.api.search.SearchResult;
import de.nitram509.mkat.api.uebersicht.Uebersicht;
import de.nitram509.videomedialist.repository.DiskContentService;
import de.nitram509.videomedialist.repository.SearchService;
import de.nitram509.mkat.repository.UebersichtService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/rest")
public class RestDispatcherServlet {

  @Inject
  UebersichtService uebersichtService;

  @Inject
  SearchService searchService;

  @Inject
  DiskContentService diskContentService;

  @GET
  @Path("uebersicht")
  @Produces({APPLICATION_JSON})
  public Uebersicht getUebersicht() {
    return uebersichtService.letzteNeuzugaenge();
  }

  @GET
  @Path("search/{query}")
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
    searchResult.setQueryTime(endTime-startTime);
    searchResult.setQuery(query);
    return Response.ok(searchResult).build();
  }

  @GET
  @Path("disk/{query}")
  @Produces({APPLICATION_JSON})
  public Response getDiskContent(@PathParam("query") String query) {
    DiskContent diskContent = diskContentService.search(query);
    SearchResult searchResult = searchService.searchByDiskId(query);
    if (searchResult.getFilme().size()>0) {
      diskContent.diskName = searchResult.getFilme().get(0).name;
      diskContent.diskId = searchResult.getFilme().get(0).id;
    }
    return Response.ok(diskContent).build();
  }
}
