package de.nitram509.videomedialist.repository.connection;

import javax.inject.Provider;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class VideoMediaListConnectionFactory implements Provider<Connection> {

  public static final String USER = "www";
  public static final String PASSW = "view";
  private static Connection connection;

  @Override
  public Connection get() {
    if (connection == null) {
      try {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost/videomedialist", USER, PASSW);
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
    return connection;
  }
}
