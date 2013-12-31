package de.nitram509.mkat.repository;

import de.nitram509.mkat.api.languages.Language;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LanguageRepository {

  @Inject
  Connection connection;

  public List<Language> findAll(){
    List<Language> languages = new ArrayList<>();
    try {
      String query = "select id,name,short,image_path from `languages` order by id asc;";
      PreparedStatement preparedStatement = null;
      try {
        preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
          Language lang = new Language();
          lang.image = resultSet.getString("image_path");
          lang.shortName = resultSet.getString("short");
          lang.name = resultSet.getString("name");
          languages.add(lang);
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
    return languages;
  }
}
