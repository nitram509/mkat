package de.nitram509.mkat.api.uebersicht;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "filmTitel")
public class FilmTitel {

  @XmlElement(name = "name")
  public String name;
  @XmlElement(name = "id")
  public int id;

  public FilmTitel() {
    super();
  }

  public FilmTitel(String name, int id) {
    this.name = name;
    this.id = id;
  }
}
