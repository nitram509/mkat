package de.nitram509.mkat.api.search;

public enum KeywordCombination {

  OR,
  AND;

  public static KeywordCombination valueFrom(String value, KeywordCombination defaultValue) {
    if (value != null) {
      value = value.toUpperCase();
      for (KeywordCombination keywordCombination : values()) {
        if (keywordCombination.toString().equals(value)) return keywordCombination;
      }
    }
    return defaultValue;
  }
}
