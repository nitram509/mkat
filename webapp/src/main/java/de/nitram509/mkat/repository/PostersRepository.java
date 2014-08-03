package de.nitram509.mkat.repository;

import de.nitram509.mkat.model.Poster;
import de.nitram509.mkat.repository.connection.MkatConnection;

import javax.inject.Inject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostersRepository {

  private Connection connection;

  public List<Poster> findByMediaId(long mediaId) {
    List<Poster> files = new ArrayList<>();
    try {
      String query = "select poster_id,media_id,name " +
          " from `posters` p" +
          " where p.media_id = ? " +
          " order by name asc";
      PreparedStatement preparedStatement = null;
      try {
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setLong(1, mediaId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
          Poster file = new Poster();
          files.add(file);
          file.poster_id = resultSet.getInt("media_id");
          file.media_id = resultSet.getInt("media_id");
          file.name = resultSet.getString("name");
        }
        resultSet.close();
      } finally {
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return files;
  }

  public void save(Poster poster) {
    String query = "insert into `posters` (" +
        " `media_id`" +
        ",`name`" +
        ") VALUES ( ? , ? )";
    try {
      PreparedStatement preparedStatement = null;
      try {
        preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        int i = 1;
        preparedStatement.setInt(i++, poster.media_id);
        preparedStatement.setString(i++, poster.name);

        preparedStatement.executeUpdate();
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
          poster.poster_id = generatedKeys.getInt(1);
        }
      } finally {
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Inject
  public void setConnection(MkatConnection connection) {
    this.connection = connection.get();
  }

}
