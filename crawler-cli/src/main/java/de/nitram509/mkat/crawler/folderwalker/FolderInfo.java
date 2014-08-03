package de.nitram509.mkat.crawler.folderwalker;

import java.io.File;
import java.util.Date;

public interface FolderInfo {

  public String getBaseFolder();

  public String getName();

  public boolean isDirectory();

  public boolean isFile();

  public File asFile();

  boolean isPoster();

  public long size();

  public Date getLastModifiedTime();
}
