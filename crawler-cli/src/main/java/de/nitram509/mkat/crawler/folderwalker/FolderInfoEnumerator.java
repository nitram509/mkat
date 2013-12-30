package de.nitram509.mkat.crawler.folderwalker;

import java.io.File;
import java.util.Enumeration;
import java.util.NoSuchElementException;

class FolderInfoEnumerator implements Enumeration<FolderInfo> {

  private final LazyWalker lazyWalker;

  private File[] candidates = new File[0];
  private int filePointer = 0;

  FolderInfoEnumerator(LazyWalker lazyWalker) {
    this.lazyWalker = lazyWalker;
  }

  @Override
  public boolean hasMoreElements() {
    boolean selfHasNext = filePointer < candidates.length;
    if (!selfHasNext) {
      while (lazyWalker.hasMoreElements()) {
        candidates = lazyWalker.nextElement();
        if (candidates.length > 0) {
          filePointer = 0;
          selfHasNext = true;
          break;
        }
      }
    }
    return selfHasNext;
  }

  @Override
  public FolderInfo nextElement() {
    if (hasMoreElements()) {
      String baseFolder = lazyWalker.getBaseFolder();
      return new FolderInfoImpl(candidates[filePointer++], baseFolder);
    }
    throw new NoSuchElementException("No more elements available.");
  }

}
