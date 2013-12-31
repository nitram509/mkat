package de.nitram509.mkat.api.search;

import de.nitram509.mkat.api.languages.Language;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;

@XmlRootElement(name = "film")
public class Film {

  @XmlElement(name = "name")
  public String name;

  @XmlElement(name = "id")
  public int id;

  @XmlElement(name = "label")
  public String label;

  @XmlElement(name = "description")
  public String description;

  @XmlElement(name = "leih")
  public String leih;

  @XmlElement(name = "updated")
  public Date updated;

  @XmlElement(name = "languages")
  public List<Language> languages;

  @XmlElement(name = "format")
  public int format;

  @XmlElement(name = "numberOfFiles")
  public int numberOfFiles;

  public Film() {
    super();
  }

  public Film(String name, int id) {
    this.name = name;
    this.id = id;
  }
}
