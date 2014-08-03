package de.nitram509.mkat.model;

public class Poster implements Cloneable {

  public int poster_id;
  public int media_id;
  public String name;

  public Poster() {
    super();
  }

  public Poster(String name, int media_id) {
    this.name = name;
    this.media_id = media_id;
  }

  @Override
  public Poster clone() {
    Poster m = new Poster();
    m.poster_id = 0;
    m.name = this.name;
    m.media_id = this.media_id;
    return m;
  }
}
