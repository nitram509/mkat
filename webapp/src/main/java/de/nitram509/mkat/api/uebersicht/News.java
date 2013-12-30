package de.nitram509.mkat.api.uebersicht;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class News {

  @XmlElement(name = "datum")
  public String datum;
  @XmlElement(name = "anzahl")
  public int anzahl;

  @XmlElement(name = "filmTitels")
  public List<FilmTitel> filmTitels = new ArrayList<>();

  public News() {
    super();
  }

  public News(String datum, int anzahl) {
    this.datum = datum;
    this.anzahl = anzahl;
  }
}
