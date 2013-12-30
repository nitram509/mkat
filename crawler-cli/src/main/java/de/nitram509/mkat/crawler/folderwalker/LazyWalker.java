package de.nitram509.mkat.crawler.folderwalker;

import java.io.File;
import java.util.Enumeration;

interface LazyWalker extends Enumeration<File[]> {

  public String getBaseFolder();

}
