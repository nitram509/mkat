package de.nitram509.mkat;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.nitram509.mkat.repository.connection.MkatConnectionFactory;
import org.testng.annotations.Test;

public class GuiceInstantiationTest {

  @Test
  public void guice_instantiation_test() {
    Injector injector = Guice.createInjector(
        new MkatAppModule()
    );

    assertDependencyIsAvailable(injector);
  }

  private void assertDependencyIsAvailable(Injector injector) {
    injector.getProvider(MkatConnectionFactory.class);
  }

}
