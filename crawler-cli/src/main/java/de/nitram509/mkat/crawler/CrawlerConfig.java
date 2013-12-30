package de.nitram509.mkat.crawler;

import java.io.File;
import java.util.Iterator;

import static java.util.Arrays.asList;

public class CrawlerConfig {

  public static final String DEFAULT_GID = "G" + System.currentTimeMillis();

  private String groupId = DEFAULT_GID;
  private String[] errorMessages;
  private String baseFolder = null;
  private boolean printHelp = false;
  private boolean md5 = false;
  private boolean verbose = false;
  private boolean includeHidden = false;

  public CrawlerConfig(String... arguments) {
    parseArguments(arguments);
    checkForErrors();
  }

  public String getGroupId() {
    return groupId;
  }

  public boolean isDryRun() {
    return DEFAULT_GID.equals(groupId);
  }

  public boolean isOK() {
    return !isError();
  }

  public boolean isPrintHelp() {
    return printHelp;
  }

  public boolean isError() {
    return errorMessages != null && errorMessages.length > 0;
  }

  public boolean isVerbose() {
    return verbose;
  }

  public boolean isMd5() {
    return md5;
  }

  public String[] getErrorMessages() {
    return errorMessages;
  }

  public String getBaseFolder() {
    return String.valueOf(baseFolder);
  }

  public boolean isIncludeHidden() {
    return includeHidden;
  }

  private void parseArguments(String... arguments) {
    Iterator<String> args = asList(arguments).iterator();
    String lastArg = null;
    while (args.hasNext()) {
      String argument = args.next();
      if (lastArg != null) {
        if ("-gid".equals(lastArg)) {
          groupId = argument;
        }
        lastArg = null;
      } else {
        switch (argument) {
          case "-gid":
            lastArg = "-gid";
            groupId = null;
            break;
          case "-md5":
            md5 = true;
            break;
          case "-all":
            includeHidden = true;
            break;
          case "-h":
            printHelp = true;
            break;
          case "-v":
            verbose = true;
            break;
          default:
            baseFolder = argument;
        }
      }
    }
  }

  private void checkForErrors() {
    if (baseFolder == null || baseFolder.isEmpty()) {
      errorMessages = new String[]{"BaseFolder must be set."};
    } else {
      File file = new File(baseFolder);
      if (!file.exists()) {
        errorMessages = new String[]{"BaseFolder doesn't exist."};
      } else if (!file.isDirectory()) {
        errorMessages = new String[]{"BaseFolder isn't a folder."};
      }
    }
    if (groupId == null || groupId.isEmpty()) {
      errorMessages = new String[]{"GroupID parameter found, but NAME is missing."};
    }
  }
}
