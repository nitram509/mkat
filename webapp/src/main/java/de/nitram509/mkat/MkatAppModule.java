package de.nitram509.mkat;

import com.google.inject.AbstractModule;
import de.nitram509.mkat.repository.LanguageRepository;
import de.nitram509.mkat.repository.connection.MkatConnection;
import de.nitram509.mkat.repository.connection.MkatConnectionFactory;

public class MkatAppModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(MkatConnection.class).toProvider(MkatConnectionFactory.class);

    bind(LanguageRepository.class);
    bind(DiskContentService.class);
    bind(SearchService.class);
  }
}
