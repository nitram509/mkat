package de.nitram509.mkat.api.search;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SearchResult {

  @XmlElement(name = "filme")
  private List<Film> filme = new ArrayList<>();

  @XmlElement(name = "query")
  private String query = "";

  @XmlElement(name = "queryTime")
  private long queryTime = 0;

  public List<Film> getFilme() {
    return filme;
  }

  @XmlElement(name = "filmeLength")
  public int getFilmeLength() {
    return filme.size();
  }

  public long getQueryTime() {
    return queryTime;
  }

  public void setQueryTime(long queryTime) {
    this.queryTime = queryTime;
  }

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }
}
