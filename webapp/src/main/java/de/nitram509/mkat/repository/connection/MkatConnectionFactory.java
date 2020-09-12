package de.nitram509.mkat.repository.connection;

import javax.inject.Provider;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MkatConnectionFactory implements Provider<MkatConnection> {

  public static final String USER = "www";
  public static final String PASSW = "view";

  private static Connection connection;

  @Override
  public MkatConnection get() {
    if (connection == null) {
      try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost/mkat", USER, PASSW);
      } catch (ClassNotFoundException | SQLException e) {
        throw new RuntimeException(e);
      }
    }
    return new MkatConnection(connection);
  }
}
