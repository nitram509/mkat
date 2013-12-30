package de.nitram509.videomedialist.repository;

import de.nitram509.mkat.api.search.Film;
import de.nitram509.mkat.api.search.SearchField;
import de.nitram509.mkat.api.search.SearchOptions;
import de.nitram509.mkat.api.search.SearchResult;
import de.nitram509.mkat.language.LanguageResolver;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static de.nitram509.mkat.api.search.KeywordCombination.AND;

public class SearchService {

  public static final String SQL_ESCAPE_CHARACTER = "\\";

  public static final String DISK_ID = "disk_id";
  public static final String DISK_NAME = "disk_name";
  public static final String UPDATED = "updated";

  @Inject
  Connection connection;

  @Inject
  LanguageResolver languageResolver;

  public SearchResult searchByDiskId(String diskId) {
    SearchOptions options = new SearchOptions();
    options.setQuery(diskId);
    options.addSearchField(SearchField.ID);
    return search(options);
  }

  public SearchResult search(SearchOptions searchOptions) {

    if ("".equals(createQuery_WhereSection(searchOptions))) {
      return new SearchResult();
    }

    String query = createQuery(searchOptions);

    try {
      PreparedStatement preparedStatement = null;
      try {
        preparedStatement = connection.prepareStatement(query);
        int i = 1;
        for (String keyword : searchOptions.getUsedKeywords()) {
          keyword = escapeSql(keyword);
          preparedStatement.setString(i++, keyword);
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        SearchResult searchResult = new SearchResult();
        while (resultSet.next()) {
          Film film = new Film();
          film.id = resultSet.getInt(DISK_ID);
          film.name = resultSet.getString(DISK_NAME);
          film.label = resultSet.getString("disk_label");
          film.languages = languageResolver.getLanguagesFromId(resultSet.getInt("language"));
          film.format = resultSet.getInt("format");
          film.updated = resultSet.getDate(UPDATED);
          film.description = resultSet.getString("description");
          film.leih = resultSet.getString("leih");
          film.numberOfFiles = resultSet.getBoolean("numberOfFiles");
          searchResult.getFilme().add(film);
        }
        resultSet.close();
        return searchResult;
      } finally {
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  String createQuery(SearchOptions searchOptions) {

    String whereSection = createQuery_WhereSection(searchOptions);

    String orderSection = createQuery_OrderSection(searchOptions);

    Set<SearchField> searchFields = searchOptions.getSearchFields();
    boolean applyEscape = (searchFields.size() == 1 && searchFields.contains(SearchField.ID)) == false;

    /*

    Example

select disk_id,disk_name,disk_label,language,format,updated,
(select count(*) from `files` f where k.disk_id = f.media_id) as numberFiles
from `katalog_videos` k
where k.disk_name like '%e%'
ORDER BY k.disk_name ASC

--> 2.5 sek. mit SQL WorkBench!

    */
    return "select disk_id,disk_name,disk_label,language,format,updated,description,leih,"
            + " (select count(*) from `files` f where k.disk_id = f.media_id) as numberOfFiles"
            + " from `katalog_videos` k"
            + " where " + whereSection.toString()
            + ((applyEscape) ? " ESCAPE '" + SQL_ESCAPE_CHARACTER + SQL_ESCAPE_CHARACTER + "' " : "")
            + " ORDER BY " + orderSection + " ASC";
  }

  String createQuery_OrderSection(SearchOptions searchOptions) {
    switch (searchOptions.getOrderByField()) {
      case NAME:
        return DISK_NAME;
      case DATUM:
        return UPDATED;
      case KOFFER:
        return DISK_ID;
      default:
        return DISK_NAME;
    }
  }

  String createQuery_WhereSection(SearchOptions searchOptions) {
    StringBuilder where = new StringBuilder();
    Set<SearchField> searchFields = searchOptions.getSearchFields();
    if (searchFields.isEmpty()) {
      return "";
    }

    List<String> usedPlaceholders = new ArrayList<>();

    for (String keyword : searchOptions.getQueryKeywords()) {
      if (where.length() > 0) {
        if (AND == searchOptions.getKeywordCombination()) {
          where.append(" and ");
        } else {
          where.append(" or ");
        }
      }

      boolean oneField = false;
      if (searchFields.contains(SearchField.ID)) {
        where.append("disk_id = ?");
        usedPlaceholders.add(keyword);
        oneField = true;
      }
      if (searchFields.contains(SearchField.NAME)) {
        if (oneField) where.append(" or ");
        where.append("disk_name like CONCAT(CONCAT('%', ?),'%')");
        usedPlaceholders.add(keyword);
        oneField = true;
      }
      if (searchFields.contains(SearchField.LABEL)) {
        if (oneField) where.append(" or ");
        where.append("disk_label like CONCAT(CONCAT('%', ?),'%')");
        usedPlaceholders.add(keyword);
        oneField = true;
      }
      if (searchFields.contains(SearchField.DESCRIPTION)) {
        if (oneField) where.append(" or ");
        where.append("description like CONCAT(CONCAT('%', ?),'%')");
        usedPlaceholders.add(keyword);
      }

    }

    String[] usedKeywordsArray = usedPlaceholders.toArray(new String[usedPlaceholders.size()]);
    searchOptions.setUsedKeywords(usedKeywordsArray);
    return where.toString();
  }

  String escapeSql(String keyword) {
    keyword = keyword.replace(SQL_ESCAPE_CHARACTER, SQL_ESCAPE_CHARACTER + SQL_ESCAPE_CHARACTER);
    keyword = keyword.replace("%", SQL_ESCAPE_CHARACTER + "%");
    keyword = keyword.replace("_", SQL_ESCAPE_CHARACTER + "_");
    return keyword;
  }

}
