package de.nitram509.mkat.crawler.folderwalker;

import de.nitram509.mkat.crawler.CrawlerConfig;

import java.io.File;
import java.util.*;

import static de.nitram509.mkat.crawler.folderwalker.FileUtils.getFiles;
import static de.nitram509.mkat.crawler.folderwalker.FileUtils.getFolders;

public class FolderWalker {

  private final CrawlerConfig crawlerConfig;
  private String baseFolder;

  public FolderWalker(CrawlerConfig crawlerConfig, String baseFolder) {
    this.baseFolder = baseFolder;
    this.crawlerConfig = crawlerConfig;
  }

  public String getBaseFolder() {
    return baseFolder;
  }

  public Enumeration<FolderInfo> recursiveWalker() {
    LazyWalker lazyWalker = new LazyWalkerImpl(crawlerConfig, baseFolder, true);
    FolderInfoEnumerator folderInfoEnumerator = new FolderInfoEnumerator(lazyWalker);
    return folderInfoEnumerator;
  }

  public Enumeration<FolderInfo> walker() {
    LazyWalker lazyWalker = new LazyWalkerImpl(crawlerConfig, baseFolder, false);
    FolderInfoEnumerator folderInfoEnumerator = new FolderInfoEnumerator(lazyWalker);
    return folderInfoEnumerator;
  }

  private static class LazyWalkerImpl implements LazyWalker {

    private final CrawlerConfig crawlerConfig;
    private final String baseFolder;
    private final boolean recursive;
    private final Queue<File> folderCandidates = new ArrayDeque<>();

    private LazyWalkerImpl(CrawlerConfig crawlerConfig, String baseFolder, boolean recursive) {
      this.crawlerConfig = crawlerConfig;
      this.baseFolder = baseFolder;
      this.recursive = recursive;
      folderCandidates.add(new File(baseFolder));
    }

    @Override
    public boolean hasMoreElements() {
      return !folderCandidates.isEmpty();
    }

    @Override
    public File[] nextElement() {
      if (hasMoreElements()) {
        return discoverFilesAndFolders(folderCandidates.poll());
      }
      throw new NoSuchElementException();
    }

    public String getBaseFolder() {
      return baseFolder;
    }

    private File[] discoverFilesAndFolders(File folder) {
      File[] files = getFiles(folder.getAbsolutePath(), crawlerConfig.isIncludeHidden());
      Arrays.sort(files);
      File[] folders = getFolders(folder.getAbsolutePath(), crawlerConfig.isIncludeHidden());
      Arrays.sort(folders);
      File[] all = new File[files.length + folders.length];
      System.arraycopy(files, 0, all, 0, files.length);
      System.arraycopy(folders, 0, all, files.length, folders.length);
      if (recursive) {
        for (File folder1 : folders) {
          folderCandidates.add(folder1);
        }
      }
      return all;
    }

  }

}
