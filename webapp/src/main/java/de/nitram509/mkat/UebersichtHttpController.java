package de.nitram509.mkat;

import de.nitram509.mkat.api.uebersicht.Uebersicht;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/uebersicht")
public class UebersichtHttpController {

  @Inject
  UebersichtService uebersichtService;

  @GET
  @Produces({APPLICATION_JSON})
  public Uebersicht getUebersicht() {
    return uebersichtService.letzteNeuzugaenge();
  }

}
