package de.nitram509.mkat;

import de.nitram509.mkat.api.uebersicht.Uebersicht;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import java.io.*;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.WILDCARD_TYPE;

@Path("/poster")
public class PosterHttpController {

  @GET
  @Path("media/{mediaId}")
  @Produces({"image/jpeg", "application/octet-stream"})
  public byte[] getPosterForMedia(@PathParam("mediaId") String mediaId) throws IOException {
    if (mediaId == null || !mediaId.matches("\\d+")) {
      throw new IllegalArgumentException("Unknown mediaId!");
    }

    File file = new File("d:\\mkat2-data\\" + mediaId + "-0001-thumb.jpg ");
    if (!file.exists()) {
      throw new FileNotFoundException("File doesn't exist!");
    }

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    FileInputStream fis = new FileInputStream(file);
    byte[] buf = new byte[8096];
    for (int read = 0; (read = fis.read(buf)) > 0; ) {
      baos.write(buf, 0, read);
    }
    fis.close();
    baos.close();
    return baos.toByteArray();
  }

}
