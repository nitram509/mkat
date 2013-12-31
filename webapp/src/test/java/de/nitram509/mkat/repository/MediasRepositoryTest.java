package de.nitram509.mkat.repository;

import de.nitram509.mkat.api.search.KeywordCombination;
import de.nitram509.mkat.api.search.SearchField;
import de.nitram509.mkat.api.search.SearchOptions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.fest.assertions.Assertions.assertThat;

public class MediasRepositoryTest {

  private MediasRepository repository;
  private SearchOptions options;

  @BeforeMethod
  public void setup() {
    repository = new MediasRepository();
    options = new SearchOptions();
  }

  @DataProvider
  public Object[][] escapingRules() {
    return new Object[][]{
            {"a ' % '", "a ' \\% '"},
            {" _ ", " \\_ "},
            {" \\ ", " \\\\ "},
    };
  }

  @Test(dataProvider = "escapingRules")
  public void escape_special_character(String query, String escapedString) {
    String actual = repository.escapeSql(query);
    assertThat(actual).isEqualTo(escapedString);
  }

  @Test
  public void create_where_statement_with_AND() {
    options.setQuery("foo bar");
    options.setKeywordCombination(KeywordCombination.AND);
    options.addSearchField(SearchField.NAME);

    String actual = repository.createQuery(options);

    actual = actual.replaceFirst("[?]", "foo");
    actual = actual.replaceFirst("[?]", "bar");
    String q = "";
    q += "name like CONCAT(CONCAT('%', foo),'%')";
    q += " and ";
    q += "name like CONCAT(CONCAT('%', bar),'%')";
    assertThat(actual).contains(q);
  }

  @Test
  public void create_where_statement_with_OR() {
    options.setQuery("foo bar");
    options.setKeywordCombination(KeywordCombination.OR);
    options.addSearchField(SearchField.NAME);

    String actual = repository.createQuery(options);

    actual = actual.replaceFirst("[?]", "foo");
    actual = actual.replaceFirst("[?]", "bar");
    String q = "";
    q += "name like CONCAT(CONCAT('%', foo),'%')";
    q += " or ";
    q += "name like CONCAT(CONCAT('%', bar),'%')";
    assertThat(actual).contains(q);
  }

  @Test
  public void create_where_statement_with_multiple_fields() {
    options.setQuery("foo bar");
    options.setKeywordCombination(KeywordCombination.AND);
    options.addSearchField(SearchField.NAME);
    options.addSearchField(SearchField.LABEL);

    String actual = repository.createQuery(options);
    actual = actual.replaceFirst("[?]", "foo");
    actual = actual.replaceFirst("[?]", "foo");
    actual = actual.replaceFirst("[?]", "bar");
    actual = actual.replaceFirst("[?]", "bar");

    String q = "";
    q += "name like CONCAT(CONCAT('%', foo),'%') or label like CONCAT(CONCAT('%', foo),'%')";
    q += " and ";
    q += "name like CONCAT(CONCAT('%', bar),'%') or label like CONCAT(CONCAT('%', bar),'%')";

    assertThat(actual).contains(q);
  }

  @Test
  public void create_where_statement_does_not_use_ESCAPE_rule_if_only_diskId_is_used() {
    options.setQuery("123456");
    options.setKeywordCombination(KeywordCombination.OR);
    options.addSearchField(SearchField.ID);

    String actual = repository.createQuery(options);
    actual = actual.replaceFirst("[?]", "123456");

    assertThat(actual).contains("media_id = 123456");
    assertThat(actual).doesNotContain("ESCAPE");
  }

}
