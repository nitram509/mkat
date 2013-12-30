package de.nitram509.mkat;

import com.google.inject.servlet.GuiceFilter;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Launcher {

  public static void main(String[] args) throws Exception {
    Server server = new Server(getPort());

    System.setProperty("java.naming.factory.initial", "org.eclipse.jetty.jndi.InitialContextFactory");

    HandlerList handlerList = new HandlerList();

    ServletContextHandler servletContextHandler = new ServletContextHandler();
    servletContextHandler.addEventListener(new MkatGuiceServletConfig());
    servletContextHandler.addFilter(GuiceFilter.class, "/rest/*", null);

    servletContextHandler.addServlet(DefaultServlet.class, "/*");
    servletContextHandler.setResourceBase("src/main/webapp");
    servletContextHandler.setWelcomeFiles(new String[]{"index.html"});

    handlerList.addHandler(servletContextHandler);

    server.setHandler(handlerList);
    server.start();
    server.join();
  }

  private static int getPort() {
    String port = System.getenv("PORT") == null ? "8080" : System.getenv("PORT");
    return Integer.parseInt(port);
  }
}