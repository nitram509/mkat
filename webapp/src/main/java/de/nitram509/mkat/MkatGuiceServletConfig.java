package de.nitram509.mkat;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import java.util.HashMap;
import java.util.Map;

public class MkatGuiceServletConfig extends GuiceServletContextListener {

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new ServletModule() {
      @Override
      protected void configureServlets() {

        install(new MkatAppModule());

        bind(SearchHttpController.class);
        bind(DiskContentHttpController.class);
        bind(UebersichtHttpController.class);
        bind(PosterHttpController.class);

        // hook Jersey into Guice Servlet
        bind(GuiceContainer.class);

        Map<String, String> params = new HashMap<>();

//        params.put("com.sun.jersey.config.property.packages", "de.nitram509.mkat");
        params.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
        params.put("com.sun.jersey.config.feature.FilterForwardOn404", "true");

//        params.put("com.sun.jersey.config.feature.Trace", "true");

        filter("/*").through(GuiceContainer.class, params);

      }
    });
  }
}
