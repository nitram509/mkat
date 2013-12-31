package de.nitram509.mkat.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.beans.Transient;
import java.util.Date;

@XmlRootElement(name = "media")
public class Media implements Cloneable {

  @XmlElement(name = "id")
  public int media_id;

  @XmlElement(name = "group_id")
  public String group_id;

  @XmlElement(name = "movie_id")
  public int movie_id;

  @XmlElement(name = "name")
  public String name;

  @XmlElement(name = "label")
  public String label;

  @XmlElement(name = "size")
  public long size;

  @XmlElement(name = "updated")
  public Date updated;

  @XmlElement(name = "description")
  public String description;

  @XmlElement(name = "languages")
  public int languages;

  @XmlElement(name = "media_type")
  public int media_type;

  @XmlElement(name = "numberOfFiles")
  public int numberOfFiles;

  public Media() {
    super();
  }

  public Media(String name, int media_id) {
    this.name = name;
    this.media_id = media_id;
  }

  @Override
  public Media clone() {
    Media m = new Media();
    m.media_id = 0;
    m.group_id = this.group_id;
    m.movie_id = this.movie_id;
    m.name = this.name;
    m.label = this.label;
    m.size = this.size;
    m.updated = this.updated;
    m.description = this.description;
    m.languages = this.languages;
    m.media_type = this.media_type;
    m.numberOfFiles = this.numberOfFiles;
    return m;
  }
}
