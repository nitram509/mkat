package de.nitram509.mkat;

import de.nitram509.mkat.api.uebersicht.FilmTitel;
import de.nitram509.mkat.api.uebersicht.News;
import de.nitram509.mkat.api.uebersicht.Uebersicht;
import de.nitram509.mkat.repository.MoviesRepository;

import javax.inject.Inject;
import java.util.List;

class UebersichtService {

  @Inject
  MoviesRepository moviesRepository;

  public Uebersicht letzteNeuzugaenge() {
    Uebersicht uebersicht = new Uebersicht();
    List<String> letzteTage = moviesRepository.getLastChangedDates();
    for (String tag : letzteTage) {
      List<FilmTitel> titel = moviesRepository.findByDay(tag);
      News news = new News(tag, titel.size());
      news.filmTitels.addAll(titel);
      uebersicht.addNews(news);
    }
    return uebersicht;
  }

}
