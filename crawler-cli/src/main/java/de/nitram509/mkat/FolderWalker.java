package de.nitram509.mkat;

import de.nitram509.mkat.crawler.CrawlerConfig;
import de.nitram509.mkat.crawler.FolderWalkerService;

import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class FolderWalker {

  private final CrawlerConfig crawlerConfig;

  public static void main(String[] args) {
    setupLogging();
    FolderWalker folderWalker = new FolderWalker(args);
    if (folderWalker.crawlerConfig.isOK()) {
      try {
        folderWalker.run();
      } catch (IOException e) {
        e.printStackTrace();
        System.exit(1);
      }
    } else {
      if (folderWalker.crawlerConfig.isError()) {
        printError(folderWalker.crawlerConfig.getErrorMessages());
        System.exit(1);
      }
      if (folderWalker.crawlerConfig.isPrintHelp()) {
        printHelp();
      }
    }
  }

  public FolderWalker(String[] args) {
    crawlerConfig = new CrawlerConfig(args);
  }

  private void run() throws IOException {
    FolderWalkerService folderWalkerService = new FolderWalkerService();
    folderWalkerService.setup(crawlerConfig);
    folderWalkerService.startWalking();
  }

  private static void printHelp() {
    System.out.println("FolderWalker 0.1");
    System.out.println("Usage:");
    System.out.println("  FolderWalker [-gid name] [-u] [-md5] [-h] baseFolder");
    System.out.println("Options:");
    System.out.println("  -gid name GroupID name for the media");
    System.out.println("  -md5      Calculate md5 checksums");
    System.out.println("  -u        Update mode");
    System.out.println("  -all      Include System and Hidden files");
    System.out.println("  -h        Print help");
    System.out.println("  -v        Verbose");
  }

  private static void printError(String... errorMessages) {
    for (String errorMessage : errorMessages) {
      System.out.println(errorMessage);
    }
  }

  private static void setupLogging() {
    try {
      LogManager.getLogManager().readConfiguration(FolderWalker.class.getResourceAsStream("/de/nitram509/mkat/logging.properties"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
