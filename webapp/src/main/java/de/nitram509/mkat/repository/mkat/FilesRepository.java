package de.nitram509.mkat.repository.mkat;

import de.nitram509.mkat.model.MkatFile;
import de.nitram509.mkat.repository.connection.MkatConnection;

import javax.inject.Inject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FilesRepository {

  private Connection connection;

  public List<MkatFile> find(long mediaId) {
    List<MkatFile> files = new ArrayList<>();
    try {
      String query = "select id,media_id,name,size,cast(addTime(date, time) as DATETIME) as `datetime`,notizen,isdir,md5 " +
          " from `files` f" +
          " where media_id=? " +
          " order by name asc";
      PreparedStatement preparedStatement = null;
      try {
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setLong(1, mediaId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
          MkatFile file = new MkatFile();
          files.add(file);
          file.id = resultSet.getLong("id");
          file.media_id = resultSet.getLong("media_id");
          file.name = resultSet.getString("name");
          file.size = resultSet.getLong("size");
          file.datetime = resultSet.getTimestamp("datetime");
          file.notizen = resultSet.getString("notizen");
          file.isdir = resultSet.getBoolean("isdir");
          file.md5 = resultSet.getString("md5");
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

  public void save(MkatFile file) {
    String query = "insert into `files` (" +
        " `media_id`" +
        ",`name`" +
        ",`size`" +
        ",`date`" +
        ",`time`" +
        ",`notizen`" +
        ",`isdir`" +
        ",`md5` " +
        ") VALUES ( ? , ? , ? , ? , ? , ? , ? , ? )";
    try {
      PreparedStatement preparedStatement = null;
      try {
        preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        int i = 1;
        preparedStatement.setLong(i++, file.media_id);
        preparedStatement.setString(i++, file.name);
        preparedStatement.setLong(i++, file.size);
        preparedStatement.setDate(i++, new Date(file.datetime.getTime()));
        preparedStatement.setTime(i++, new Time(file.datetime.getTime()));
        preparedStatement.setString(i++, file.notizen);
        preparedStatement.setShort(i++, (short) (file.isdir ? 1 : 0));
        preparedStatement.setString(i++, file.md5);

        preparedStatement.executeUpdate();
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
          file.id = generatedKeys.getLong(1);
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
