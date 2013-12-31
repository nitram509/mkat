package de.nitram509.videomedialist.repository;

import de.nitram509.mkat.model.Media;
import de.nitram509.mkat.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class Migrated {

  private List<Media> medias = new ArrayList<>();
  private List<Movie> movies = new ArrayList<>();

  public Migrated(Media media, Movie movie) {
    this.medias.add(media);
    this.movies.add(movie);
  }

  public Migrated(Media media) {
    this.medias.add(media);
  }

  public List<Media> getMedias() {
    return medias;
  }

  public List<Movie> getMovies() {
    return movies;
  }
}
