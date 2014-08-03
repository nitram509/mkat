package de.nitram509.mkat.crawler;

import de.nitram509.mkat.model.Media;
import de.nitram509.mkat.model.MkatFile;
import de.nitram509.mkat.model.Movie;

import java.io.File;

public class DiscoveryResult {

  public Media media;
  public Movie movie;
  public MkatFile[] files = new MkatFile[0];
  public File[] posters = new File[0];

  public void add(MkatFile file) {
    MkatFile[] tmp = new MkatFile[files.length + 1];
    System.arraycopy(files, 0, tmp, 0, files.length);
    tmp[files.length] = file;
    files = tmp;
  }

  public void addPoster(File file) {
    File[] tmp = new File[posters.length + 1];
    System.arraycopy(posters, 0, tmp, 0, posters.length);
    tmp[posters.length] = file;
    posters = tmp;
  }
}
