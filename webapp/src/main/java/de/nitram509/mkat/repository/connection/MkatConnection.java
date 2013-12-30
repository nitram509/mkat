package de.nitram509.mkat.repository.connection;

import javax.inject.Provider;
import java.sql.Connection;

public class MkatConnection implements Provider<Connection> {

  private final Connection connection;

  MkatConnection(Connection connection) {
    this.connection = connection;
  }

  @Override
  public Connection get() {
    return connection;
  }
}
