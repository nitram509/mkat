package de.nitram509.mkat.model;

import de.nitram509.mkat.api.languages.Language;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;

@XmlRootElement(name = "movie")
public class Movie {

  @XmlElement(name = "movie_id")
  public int movie_id = 0;

  @XmlElement(name = "name")
  public String name;

  @XmlElement(name = "languages")
  public int languages;

  @XmlElement(name = "format")
  public int format;

  @XmlElement(name = "description")
  public String description;

  @XmlElement(name = "updated")
  public Date updated;
}

