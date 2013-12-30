package de.nitram509.mkat.crawler.folderwalker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

class FolderInfoImpl implements FolderInfo {

  private final File candidate;
  private final String baseFolder;
  private final String name;

  public FolderInfoImpl(File candidate, String baseFolder) {
    this.candidate = candidate;
    this.baseFolder = baseFolder;
    this.name = normalizedName();
  }

  private String normalizedName() {
    String relativePathName = new File(baseFolder).toPath().relativize(candidate.toPath()).toString();
    relativePathName = relativePathName.replace(File.separatorChar, '/');
    return relativePathName;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getBaseFolder() {
    return baseFolder;
  }

  @Override
  public boolean isDirectory() {
    return candidate.isDirectory();
  }

  @Override
  public boolean isFile() {
    return candidate.isFile();
  }

  @Override
  public File asFile() {
    return candidate;
  }

  @Override
  public long size() {
    if (isFile()) {
      try {
        return Files.size(asFile().toPath());
      } catch (IOException e) {
        return -1L;
      }
    }
    return 0L;
  }

  @Override
  public Date getLastModifiedTime() {
    try {
      return new Date(Files.getLastModifiedTime(asFile().toPath()).toMillis());
    } catch (IOException e) {
      return new Date(System.currentTimeMillis());
    }
  }

}
