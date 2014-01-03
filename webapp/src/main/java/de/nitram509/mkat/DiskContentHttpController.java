package de.nitram509.mkat;

import de.nitram509.mkat.api.diskcontent.DiskContent;
import de.nitram509.mkat.api.search.SearchResult;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/disk")
public class DiskContentHttpController {

  @Inject
  SearchService searchService;

  @Inject
  DiskContentService diskContentService;

  @GET
  @Path("{query}")
  @Produces({APPLICATION_JSON})
  public Response getDiskContent(@PathParam("query") String query) {
    DiskContent diskContent = diskContentService.search(query);
    SearchResult searchResult = searchService.searchByDiskId(query);
    if (searchResult.getFilme().size() > 0) {
      diskContent.diskName = searchResult.getFilme().get(0).name;
      diskContent.diskId = searchResult.getFilme().get(0).id;
    }
    return Response.ok(diskContent).build();
  }
}
