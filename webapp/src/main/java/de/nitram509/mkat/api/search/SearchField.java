package de.nitram509.mkat.api.search;

public enum SearchField {
  
  ID,
  NAME,
  LABEL,
  DESCRIPTION;

  public static SearchField valueFrom(String value, SearchField defaultValue) {
    if (value != null) {
      value = value.toUpperCase();
      for (SearchField SearchField : values()) {
        if (SearchField.toString().equals(value)) return SearchField;
      }
    }
    return defaultValue;
  }
  
}
