package de.nitram509.mkat.api.search;

import java.util.HashSet;
import java.util.Set;

import static de.nitram509.mkat.api.search.KeywordCombination.OR;
import static de.nitram509.mkat.api.search.KeywordCombination.valueFrom;

public class SearchOptions {

  private String query;
  private KeywordCombination keywordCombination = OR;
  private OrderByField orderByField = OrderByField.NAME;
  private Set<SearchField> searchFields = new HashSet<>(2);
  private String[] usedKeywords = new String[0];

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public String[] getQueryKeywords() {
    return query != null ? query.split("\\s") : null;
  }

  public String[] getUsedKeywords() {
    return usedKeywords;
  }

  public void setUsedKeywords(String[] usedPlaceholder) {
    this.usedKeywords = usedPlaceholder;
  }

  public KeywordCombination getKeywordCombination() {
    return keywordCombination;
  }

  public void setKeywordCombination(KeywordCombination keywordCombination) {
    this.keywordCombination = keywordCombination;
  }

  public void setKeywordCombination(String keywordCombination) {
    this.keywordCombination = valueFrom(keywordCombination, OR);
  }

  public OrderByField getOrderByField() {
    return orderByField;
  }

  public void setOrderByField(OrderByField orderByField) {
    this.orderByField = orderByField;
  }

  public void setOrderByField(String orderByField) {
    this.orderByField = OrderByField.valueFrom(orderByField, OrderByField.NAME);
  }

  public void addSearchField(SearchField searchField) {
    if (searchField != null) {
      searchFields.add(searchField);
    }
  }

  public void addSearchField(String searchFieldString) {
    addSearchField(SearchField.valueFrom(searchFieldString, null));
  }

  public Set<SearchField> getSearchFields() {
    return searchFields;
  }

  public void addDefaultSearchFields() {
    searchFields.add(SearchField.ID);
    searchFields.add(SearchField.NAME);
  }


}
