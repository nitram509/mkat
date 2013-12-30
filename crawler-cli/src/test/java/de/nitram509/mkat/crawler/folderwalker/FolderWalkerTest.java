package de.nitram509.mkat.crawler.folderwalker;

import de.nitram509.mkat.crawler.CrawlerConfig;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.fest.assertions.Assertions.assertThat;

public class FolderWalkerTest {

  public static final String File_1 = "f1";
  public static final String File_2 = "f2";
  private FolderWalker folderWalker;
  private File testRootFolder;
  private List<File> temporaryFiles = new ArrayList<File>();
  private CrawlerConfig crawlerConfig;

  @BeforeMethod
  public void setUp() throws Exception {
    Path tempDirectory = Files.createTempDirectory("mkat-test");
    testRootFolder = tempDirectory.toFile();
    testRootFolder.deleteOnExit();

    crawlerConfig = new CrawlerConfig();
    folderWalker = new FolderWalker(crawlerConfig, testRootFolder.getAbsolutePath());
  }

  @AfterMethod(alwaysRun = true)
  public void cleanup_all_temporary_files() {
    Collections.reverse(temporaryFiles);
    Iterator<File> it = temporaryFiles.iterator();
    while (it.hasNext()) {
      it.next().delete();
    }
  }

  @Test
  public void getter_works() {
    folderWalker = new FolderWalker(crawlerConfig, ".");
    assertThat(folderWalker.getBaseFolder()).isEqualTo(".");
  }

  @Test
  public void traversal_in_one_folder() throws IOException {
    // given
    createTempFile(File_1);
    createTempFile(File_2);

    // when
    Enumeration<FolderInfo> walker = folderWalker.recursiveWalker();

    // then
    {
      assertThat(walker.hasMoreElements()).isTrue();
      FolderInfo w = walker.nextElement();
      assertThat(w.getName()).isEqualTo(File_1);
      assertThat(w.getBaseFolder()).isEqualTo(testRootFolder.getAbsolutePath());
    }

    assertThat(walker.hasMoreElements()).isTrue();
    assertThat(walker.nextElement().getName()).isEqualTo(File_2);

    assertThat(walker.hasMoreElements()).isFalse();
  }

  @Test
  public void traversal_recursive_in_one_folder_and_subfolder() throws IOException {
    // given
    createTempFolderStructure("sub1", "sub2", File_1);
    createTempFolderStructure("sub3", "sub4", File_2);

    // when
    Enumeration<FolderInfo> walker = folderWalker.recursiveWalker();

    // then
    {
      assertThat(walker.hasMoreElements()).isTrue();
      FolderInfo w = walker.nextElement();
      assertThat(w.getName()).isEqualTo("sub1");
      assertThat(w.isDirectory()).isTrue();
    }
    {
      assertThat(walker.hasMoreElements()).isTrue();
      FolderInfo w = walker.nextElement();
      assertThat(w.getName()).isEqualTo("sub3");
      assertThat(w.isDirectory()).isTrue();
    }
    {
      assertThat(walker.hasMoreElements()).isTrue();
      FolderInfo w = walker.nextElement();
      assertThat(w.getName()).isEqualTo("sub1/sub2");
      assertThat(w.isDirectory()).isTrue();
    }
    {
      assertThat(walker.hasMoreElements()).isTrue();
      FolderInfo w = walker.nextElement();
      assertThat(w.getName()).isEqualTo("sub3/sub4");
      assertThat(w.isDirectory()).isTrue();
    }
    {
      assertThat(walker.hasMoreElements()).isTrue();
      FolderInfo w = walker.nextElement();
      assertThat(w.getName()).isEqualTo("sub1/sub2/" + File_1);
      assertThat(w.isFile()).isTrue();
    }
    {
      assertThat(walker.hasMoreElements()).isTrue();
      FolderInfo w = walker.nextElement();
      assertThat(w.getName()).isEqualTo("sub3/sub4/" + File_2);
      assertThat(w.isFile()).isTrue();
    }
    assertThat(walker.hasMoreElements()).isFalse();
  }

  @Test
  public void traversal_NOT_recursive_in_one_folder_and_subfolder() throws IOException {
    // given
    createTempFolderStructure(File_1);
    createTempFolderStructure("sub1", "sub2", File_2);

    // when
    Enumeration<FolderInfo> walker = folderWalker.walker();

    // then
    {
      assertThat(walker.hasMoreElements()).isTrue();
      FolderInfo w = walker.nextElement();
      assertThat(w.getName()).isEqualTo(File_1);
      assertThat(w.isFile()).isTrue();
    }
    {
      assertThat(walker.hasMoreElements()).isTrue();
      FolderInfo w = walker.nextElement();
      assertThat(w.getName()).isEqualTo("sub1");
      assertThat(w.isDirectory()).isTrue();
    }
    assertThat(walker.hasMoreElements()).isFalse();
  }

  private void createTempFile(String file) throws IOException {
    File f = new File(testRootFolder, file);
    f.createNewFile();
    temporaryFiles.add(f);
  }

  private void createTempFolderStructure(String... fileOrPath) throws IOException {
    if (fileOrPath.length == 0) return;
    File f = testRootFolder;
    for (int i = 0; i < fileOrPath.length - 1; i++) {
      f = new File(f, fileOrPath[i]);
      f.mkdir();
      temporaryFiles.add(f);
    }
    f = new File(f, fileOrPath[fileOrPath.length - 1]);
    f.createNewFile();
    temporaryFiles.add(f);
  }

}
