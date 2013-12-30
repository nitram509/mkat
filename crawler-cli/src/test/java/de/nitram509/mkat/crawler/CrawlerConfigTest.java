package de.nitram509.mkat.crawler;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.fest.assertions.Assertions.assertThat;

public class CrawlerConfigTest {

  private String testBaseFolder;

  @BeforeMethod
  public void setup() throws IOException {
    File testBaseFolderFile = Files.createTempDirectory("test").toFile();
    testBaseFolderFile.deleteOnExit();
    testBaseFolder = testBaseFolderFile.getAbsolutePath();
  }

  @Test
  public void isPrintHelp() {
    {
      CrawlerConfig crawler = new CrawlerConfig("-h");
      assertThat(crawler.isPrintHelp()).isTrue();
      assertThat(crawler.isOK()).isFalse();
    }
  }

  @Test
  public void GroupID_is_set() {
    CrawlerConfig crawler = new CrawlerConfig("-gid", "FooBar", testBaseFolder);
    assertThat(crawler.isPrintHelp()).isFalse();
    assertThat(crawler.isOK()).isTrue();
    assertThat(crawler.getGroupId()).isEqualTo("FooBar");
  }

  @Test
  public void GroupID_missing() {
    CrawlerConfig crawler = new CrawlerConfig("-gid");
    assertThat(crawler.isError()).isTrue();
    assertThat(crawler.isPrintHelp()).isFalse();
    assertThat(crawler.isOK()).isFalse();
    assertThat(crawler.getErrorMessages()).contains("GroupID parameter found, but NAME is missing.");
  }

  @Test
  public void BaseFolder_must_be_set() {
    CrawlerConfig crawler = new CrawlerConfig();
    assertThat(crawler.isError()).isTrue();
    assertThat(crawler.isPrintHelp()).isFalse();
    assertThat(crawler.isOK()).isFalse();
    assertThat(crawler.getErrorMessages()).contains("BaseFolder must be set.");
  }

  @Test
  public void BaseFolder_is_set() throws IOException {
    CrawlerConfig crawler = new CrawlerConfig("-gid", "anyGid", testBaseFolder);

    assertThat(crawler.isError()).isFalse();
    assertThat(crawler.isPrintHelp()).isFalse();
    assertThat(crawler.isOK()).isTrue();
    assertThat(crawler.isDryRun()).isFalse();
    assertThat(crawler.getBaseFolder()).isEqualTo(testBaseFolder);
  }

  @Test
  public void dryRun_if_GroupID_is_default_and_not_set() {
    CrawlerConfig crawler = new CrawlerConfig(testBaseFolder);

    assertThat(crawler.isError()).isFalse();
    assertThat(crawler.isPrintHelp()).isFalse();
    assertThat(crawler.isOK()).isTrue();
    assertThat(crawler.isDryRun()).isTrue();
  }

  @Test
  public void BaseFolder_does_not_exists() {
    CrawlerConfig crawler = new CrawlerConfig("not exists");

    assertThat(crawler.isError()).isTrue();
    assertThat(crawler.getErrorMessages()).contains("BaseFolder doesn't exist.");
  }

  @Test
  public void MD5_is_set() {
    CrawlerConfig crawler = new CrawlerConfig("-md5", testBaseFolder);

    assertThat(crawler.isError()).isFalse();
    assertThat(crawler.isMd5()).isTrue();
  }

  @Test
  public void VERBOSE_is_set() {
    CrawlerConfig crawler = new CrawlerConfig("-v", testBaseFolder);

    assertThat(crawler.isError()).isFalse();
    assertThat(crawler.isVerbose()).isTrue();
  }

  @Test
  public void IncludeSystemAndHiddenFiles_is_set() {
    CrawlerConfig crawler = new CrawlerConfig("-all", testBaseFolder);

    assertThat(crawler.isError()).isFalse();
    assertThat(crawler.isIncludeHidden()).isTrue();
  }

}
