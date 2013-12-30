package de.nitram509.mkat.crawler.folderwalker;

import java.util.logging.ConsoleHandler;

public class ConsoleLogHandler extends ConsoleHandler {

  public ConsoleLogHandler() {
    super();
    setOutputStream(System.out);
  }
}
