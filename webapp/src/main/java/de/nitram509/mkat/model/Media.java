package de.nitram509.mkat.model;

import java.util.Date;

public class Media implements Cloneable {

  public int media_id;
  public String group_id;
  public int movie_id;
  public String name;
  public String label;
  public long size;
  public Date updated;
  public String description;
  public int languages;
  public int media_type;
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
