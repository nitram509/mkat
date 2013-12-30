package de.nitram509.videomedialist.repository;

import de.nitram509.mkat.api.search.KeywordCombination;
import de.nitram509.mkat.api.search.SearchField;
import de.nitram509.mkat.api.search.SearchOptions;
import de.nitram509.videomedialist.repository.SearchService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.fest.assertions.Assertions.assertThat;

public class SearchServiceTest {

  private SearchService searchService;
  private SearchOptions options;

  @BeforeMethod
  public void setup() {
    searchService = new SearchService();
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
    String actual = searchService.escapeSql(query);
    assertThat(actual).isEqualTo(escapedString);
  }

  @Test
  public void create_where_statement_with_AND() {
    options.setQuery("foo bar");
    options.setKeywordCombination(KeywordCombination.AND);
    options.addSearchField(SearchField.NAME);

    String actual = searchService.createQuery(options);

    actual = actual.replaceFirst("[?]", "foo");
    actual = actual.replaceFirst("[?]", "bar");
    String q = "";
    q += "disk_name like CONCAT(CONCAT('%', foo),'%')";
    q += " and ";
    q += "disk_name like CONCAT(CONCAT('%', bar),'%')";
    assertThat(actual).contains(q);
  }

  @Test
  public void create_where_statement_with_OR() {
    options.setQuery("foo bar");
    options.setKeywordCombination(KeywordCombination.OR);
    options.addSearchField(SearchField.NAME);

    String actual = searchService.createQuery(options);

    actual = actual.replaceFirst("[?]", "foo");
    actual = actual.replaceFirst("[?]", "bar");
    String q = "";
    q += "disk_name like CONCAT(CONCAT('%', foo),'%')";
    q += " or ";
    q += "disk_name like CONCAT(CONCAT('%', bar),'%')";
    assertThat(actual).contains(q);
  }

  @Test
  public void create_where_statement_with_multiple_fields() {
    options.setQuery("foo bar");
    options.setKeywordCombination(KeywordCombination.AND);
    options.addSearchField(SearchField.NAME);
    options.addSearchField(SearchField.LABEL);

    String actual = searchService.createQuery(options);
    actual = actual.replaceFirst("[?]", "foo");
    actual = actual.replaceFirst("[?]", "foo");
    actual = actual.replaceFirst("[?]", "bar");
    actual = actual.replaceFirst("[?]", "bar");

    String q = "";
    q += "disk_name like CONCAT(CONCAT('%', foo),'%') or disk_label like CONCAT(CONCAT('%', foo),'%')";
    q += " and ";
    q += "disk_name like CONCAT(CONCAT('%', bar),'%') or disk_label like CONCAT(CONCAT('%', bar),'%')";

    assertThat(actual).contains(q);
  }

  @Test
  public void create_where_statement_does_not_use_ESCAPE_rule_if_only_diskId_is_used() {
    options.setQuery("123456");
    options.setKeywordCombination(KeywordCombination.OR);
    options.addSearchField(SearchField.ID);

    String actual = searchService.createQuery(options);
    actual = actual.replaceFirst("[?]", "123456");

    assertThat(actual).contains("disk_id = 123456");
    assertThat(actual).doesNotContain("ESCAPE");
  }
}
