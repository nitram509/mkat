package de.nitram509.videomedialist.repository;

import de.nitram509.mkat.api.search.KeywordCombination;
import de.nitram509.mkat.api.search.OrderByField;
import de.nitram509.mkat.api.search.SearchOptions;
import de.nitram509.mkat.api.search.SearchResult;
import de.nitram509.mkat.language.LanguageResolver;
import de.nitram509.videomedialist.repository.connection.VideoMediaListConnectionFactory;
import de.nitram509.videomedialist.repository.SearchService;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertNotNull;

public class SearchServiceLearningTest {

  private SearchService searchService;

  @BeforeMethod
  public void setup() {
    searchService = new SearchService();
    searchService.connection = new VideoMediaListConnectionFactory().get();
    searchService.languageResolver = mock(LanguageResolver.class);
  }

  @AfterClass
  public void afterMethod() throws SQLException {
    searchService.connection.close();
  }

  @Test
  public void do_actual_simple_search_on_database() {

    SearchOptions searchOptions = new SearchOptions();
    searchOptions.setKeywordCombination(KeywordCombination.AND);
    searchOptions.setOrderByField(OrderByField.NAME);
    searchOptions.setQuery("xxx");

    SearchResult result = searchService.search(searchOptions);
    assertNotNull(result);
  }

  @Test
  public void do_search_with_ID_only_on_database() {

    SearchOptions searchOptions = new SearchOptions();
    searchOptions.setKeywordCombination(KeywordCombination.OR);
    searchOptions.setOrderByField(OrderByField.NAME);
    searchOptions.setQuery("133798");
    searchOptions.addSearchField("ID");

    SearchResult result = searchService.search(searchOptions);
    assertNotNull(result);
  }

}
