package de.nitram509.mkat.crawler;

import de.nitram509.mkat.model.MkatFile;
import de.nitram509.mkat.model.Media;
import de.nitram509.mkat.model.Movie;

public class DiscoveryResult {

  public Media media;
  public Movie movie;
  public MkatFile[] files = new MkatFile[0];

  public void add(MkatFile file) {
    MkatFile[] tmp = new MkatFile[files.length + 1];
    System.arraycopy(files, 0, tmp, 0, files.length);
    tmp[files.length] = file;
    files = tmp;
  }

}
