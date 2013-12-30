package de.nitram509.mkat.api.search;

public enum OrderByField {

  NAME,
  KOFFER,
  DATUM;

  public static OrderByField valueFrom(String value, OrderByField defaultValue) {
    if (value != null) {
      value = value.toUpperCase();
      for (OrderByField OrderByField : values()) {
        if (OrderByField.toString().equals(value)) return OrderByField;
      }
    }
    return defaultValue;
  }

}
