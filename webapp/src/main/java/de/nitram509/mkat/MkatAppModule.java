package de.nitram509.mkat;

import com.google.inject.AbstractModule;
import de.nitram509.mkat.repository.connection.MkatConnection;
import de.nitram509.mkat.repository.connection.MkatConnectionFactory;
import de.nitram509.videomedialist.repository.connection.VideoMediaListConnectionFactory;
import de.nitram509.videomedialist.repository.DiskContentService;
import de.nitram509.videomedialist.repository.LanguageService;
import de.nitram509.videomedialist.repository.SearchService;

import java.sql.Connection;

public class MkatAppModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(Connection.class).toProvider(VideoMediaListConnectionFactory.class);
    bind(MkatConnection.class).toProvider(MkatConnectionFactory.class);

    bind(LanguageService.class);
    bind(DiskContentService.class);
    bind(SearchService.class);
  }
}
