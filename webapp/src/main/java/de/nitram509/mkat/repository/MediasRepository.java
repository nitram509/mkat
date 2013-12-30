package de.nitram509.mkat.repository;

import de.nitram509.mkat.api.search.SearchField;
import de.nitram509.mkat.api.search.SearchOptions;
import de.nitram509.mkat.model.Media;
import de.nitram509.mkat.repository.connection.MkatConnection;

import javax.inject.Inject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static de.nitram509.mkat.api.search.KeywordCombination.AND;

public class MediasRepository {


  public static final String SQL_ESCAPE_CHARACTER = "\\";
  public static final String MEDIA_NAME = "NAME";
  public static final String MEDIA_GROUP_ID = "group_id";
  public static final String UPDATED = "updated";
  public static final String MEDIA_LABEL = "LABEL";

  private Connection connection;

  public List<Media> findByDiskId(String diskId) {
    SearchOptions options = new SearchOptions();
    options.setQuery(diskId);
    options.addSearchField(SearchField.ID);
    return find(options);
  }

  public List<Media> find(SearchOptions searchOptions) {

    if ("".equals(createQuery_WhereSection(searchOptions))) {
      return new ArrayList<>();
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
        return runPreparedStatement(preparedStatement);
      } finally {
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public List<Media> findAll() {

    String query = "select media_id,group_id,movie_id,name,label,language,mediatype,updated,description,size"
        + " from `medias` k"
        + " ORDER BY MEDIA_ID ASC";

    try {
      PreparedStatement preparedStatement = null;
      try {
        preparedStatement = connection.prepareStatement(query);
        return runPreparedStatement(preparedStatement);
      } finally {
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void save(Media media) {
    if (media.media_id == 0) {
      insert(media);
    } else {
      update(media);
    }
  }

  private void insert(Media media) {
    String query = "insert into `medias` (" +
        "`group_id`" +
        ",`movie_id`" +
        ",`name`" +
        ",`label`" +
        ",`size`" +
        ",`updated`" +
        ",`language`" +
        ",`mediatype` " +
        ",`description` " +
        ") VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
    try {
      PreparedStatement preparedStatement = null;
      try {
        preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        int i = 1;
        preparedStatement.setString(i++, media.group_id);
        preparedStatement.setInt(i++, media.movie_id);
        preparedStatement.setString(i++, media.name);
        preparedStatement.setString(i++, media.label.substring(0, Math.min(50, media.label.length())));
        preparedStatement.setLong(i++, media.size);
        preparedStatement.setTimestamp(i++, new Timestamp(media.updated != null ? media.updated.getTime() : System.currentTimeMillis()));
        preparedStatement.setInt(i++, media.languages);
        preparedStatement.setInt(i++, media.media_type);
        preparedStatement.setString(i++, media.description);

        preparedStatement.executeUpdate();
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
          media.media_id = generatedKeys.getInt(1);
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

  private void update(Media media) {
    String query = "UPDATE `medias` SET " +
        " `group_id` = ?" +
        ",`movie_id` = ?" +
        ",`name` = ?" +
        ",`label` = ?" +
        ",`size` = ?" +
        ",`updated` = ?" +
        ",`language` = ?" +
        ",`mediatype` = ?" +
        ",`description` = ?" +
        "WHERE media_id = ? ";
    try {
      PreparedStatement preparedStatement = null;
      try {
        preparedStatement = connection.prepareStatement(query);
        int i = 1;
        preparedStatement.setString(i++, media.group_id);
        preparedStatement.setInt(i++, media.movie_id);
        preparedStatement.setString(i++, media.name);
        preparedStatement.setString(i++, media.label.substring(0, Math.min(50, media.label.length())));
        preparedStatement.setLong(i++, media.size);
        preparedStatement.setTimestamp(i++, new Timestamp(media.updated != null ? media.updated.getTime() : System.currentTimeMillis()));
        preparedStatement.setInt(i++, media.languages);
        preparedStatement.setInt(i++, media.media_type);
        preparedStatement.setString(i++, media.description);
        preparedStatement.setInt(i++, media.media_id);

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

  public void delete(Media media) {
    try {
      PreparedStatement preparedStatement = null;
      try {
        preparedStatement = connection.prepareStatement("delete from medias where media_id = ?");
        preparedStatement.setInt(1, media.media_id);
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

  private List<Media> runPreparedStatement(PreparedStatement preparedStatement) throws SQLException {
    ResultSet resultSet = preparedStatement.executeQuery();
    List<Media> medias = new ArrayList<>();
    while (resultSet.next()) {
      Media media = new Media();
      media.media_id = resultSet.getInt("MEDIA_ID");
      media.movie_id = resultSet.getInt("MOVIE_ID");
      media.name = resultSet.getString(MEDIA_NAME);
      media.group_id = resultSet.getString(MEDIA_GROUP_ID);
      media.label = resultSet.getString(MEDIA_LABEL);
      media.languages = resultSet.getInt("language");
      media.media_type = resultSet.getInt("mediatype");
      media.updated = resultSet.getTimestamp(UPDATED);
      media.description = resultSet.getString("description");
      media.size = resultSet.getLong("size");
      medias.add(media);
    }
    resultSet.close();
    return medias;
  }

  String createQuery(SearchOptions searchOptions) {

    String whereSection = createQuery_WhereSection(searchOptions);

    String orderSection = createQuery_OrderSection(searchOptions);

    Set<SearchField> searchFields = searchOptions.getSearchFields();
    boolean applyEscape = (searchFields.size() == 1 && searchFields.contains(SearchField.ID)) == false;

    return "select disk_id,disk_name,disk_label,language,mediatype,updated,description,leih,"
        + " (select count(*) from `files` f where k.disk_id = f.media_id) as numberOfFiles"
        + " from `katalog_videos` k"
        + " where " + whereSection.toString()
        + ((applyEscape) ? " ESCAPE '" + SQL_ESCAPE_CHARACTER + SQL_ESCAPE_CHARACTER + "' " : "")
        + " ORDER BY " + orderSection + " ASC";
  }

  String createQuery_OrderSection(SearchOptions searchOptions) {
    switch (searchOptions.getOrderByField()) {
      case NAME:
        return MEDIA_NAME;
      case DATUM:
        return UPDATED;
      case KOFFER:
        return MEDIA_GROUP_ID;
      default:
        return MEDIA_NAME;
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

  @Inject
  public void setConnection(MkatConnection connection) {
    this.connection = connection.get();
  }

}
