package de.nitram509.videomedialist.repository;

import de.nitram509.mkat.api.diskcontent.DiskContent;
import de.nitram509.mkat.api.diskcontent.FileInfo;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DiskContentService {

  @Inject
  Connection connection;

  public DiskContent search(String diskId) {
    DiskContent diskContent = new DiskContent();
    try {
      String query = "select name,size,cast(addTime(date, time) as DATETIME) as `datetime`,notizen,isdir,md5 " +
              " from `files` f" +
              " where media_id=? " +
              " order by name asc";
      PreparedStatement preparedStatement = null;
      try {
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, diskId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
          FileInfo file = new FileInfo();
          diskContent.files.add(file);
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
    return diskContent;
  }

}
