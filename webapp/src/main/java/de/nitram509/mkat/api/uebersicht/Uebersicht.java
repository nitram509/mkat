package de.nitram509.mkat.api.uebersicht;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "uebersicht")
public class Uebersicht {

  @XmlElement(name = "news")
  private News[] news = new News[0];

  public void addNews(News news) {
    final News[] temporary = new News[this.news.length + 1];
    System.arraycopy(this.news, 0, temporary, 0, this.news.length);
    this.news = temporary;
    this.news[this.news.length - 1] = news;
  }

  public News[] getNews() {
    return this.news;
  }

}
