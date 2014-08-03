package de.nitram509.mkat.repository;

import de.nitram509.mkat.api.uebersicht.FilmTitel;
import de.nitram509.mkat.model.Movie;
import de.nitram509.mkat.repository.connection.MkatConnection;

import javax.inject.Inject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MoviesRepository {

  private Connection connection;

  public List<Movie> findAll() {
    List<Movie> movies = new ArrayList<>();
    try {
      String query = "select movie_id,name,languages,format,description,updated " +
              " from `movies` f";
      PreparedStatement preparedStatement = null;
      try {
        preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
          Movie movie = new Movie();
          movies.add(movie);
          movie.movie_id = resultSet.getInt("movie_id");
          movie.name = resultSet.getString("name").trim();
          movie.languages = resultSet.getInt("languages");
          movie.format = resultSet.getInt("format");
          movie.updated = resultSet.getDate("updated");
          movie.description = resultSet.getString("description");
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
    return movies;
  }

  public List<FilmTitel> findByDay(String isoday) {
    List<FilmTitel> titel = new ArrayList<>();
    try {
      String query = "select " +
              "movie_id," +
              "name, " +
              "(select media_id from medias k where k.MOVIE_ID = m.MOVIE_ID) as media_id "+
              "from `movies` m " +
              "where left(updated,10) = ? " +
              "order by name asc;";
      PreparedStatement preparedStatement = null;
      try {
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, isoday);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
          String movie_name = resultSet.getString("name");
          int movie_id = resultSet.getInt("movie_id");
          int media_id = resultSet.getInt("media_id");
          titel.add(new FilmTitel(movie_name, media_id));
        }
        resultSet.close();
      } finally {
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
    return titel;
  }


  public List<String> getLastChangedDates() {
    List<String> days = new ArrayList<>();
    try {
      String query = "select distinct (left(updated,10)) as updated from movies order by updated desc limit 3";
      PreparedStatement preparedStatement = null;
      try {
        preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
          days.add(resultSet.getString("updated"));
        }
        resultSet.close();
      } finally {
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
    return days;
  }

  public void insert(Movie movie) {
    String query = "insert into `movies` (" +
        " `name`" +
        ",`languages`" +
        ",`format`" +
        ",`description`" +
        ",`updated`" +
        ") VALUES ( ? , ? , ? , ? , ? )";
    try {
      PreparedStatement preparedStatement = null;
      try {
        preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        int i = 1;
        preparedStatement.setString(i++, movie.name);
        preparedStatement.setInt(i++, movie.languages);
        preparedStatement.setInt(i++, movie.format);
        preparedStatement.setString(i++, movie.description);
        preparedStatement.setTimestamp(i++, new Timestamp(movie.updated != null ? movie.updated.getTime() : System.currentTimeMillis()));
        preparedStatement.executeUpdate();
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
          movie.movie_id = generatedKeys.getInt(1);
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

  public void update(Movie movie) {
    String query = "insert into `movies` (" +
        " `movie_id`" +
        " `name`" +
        ",`languages`" +
        ",`format`" +
        ",`description`" +
        ",`updated`" +
        ") VALUES ( ? , ? , ? , ? , ? , ? )";
    try {
      PreparedStatement preparedStatement = null;
      try {
        preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        int i = 1;
        preparedStatement.setInt(i++, movie.movie_id);
        preparedStatement.setString(i++, movie.name);
        preparedStatement.setInt(i++, movie.languages);
        preparedStatement.setInt(i++, movie.format);
        preparedStatement.setString(i++, movie.description);
        preparedStatement.setTimestamp(i++, new Timestamp(movie.updated != null ? movie.updated.getTime() : System.currentTimeMillis()));
        preparedStatement.executeUpdate();
      } finally {
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void delete(Movie movie) {
    try {
      PreparedStatement preparedStatement = null;
      try {
        preparedStatement = connection.prepareStatement("delete from `movies` where movie_id = ? ");
        preparedStatement.setInt(1, movie.movie_id);
        preparedStatement.executeUpdate();
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
