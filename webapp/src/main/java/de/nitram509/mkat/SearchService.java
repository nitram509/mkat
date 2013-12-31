package de.nitram509.mkat;

import de.nitram509.mkat.api.search.Film;
import de.nitram509.mkat.api.search.SearchOptions;
import de.nitram509.mkat.api.search.SearchResult;
import de.nitram509.mkat.language.LanguageResolver;
import de.nitram509.mkat.model.Media;
import de.nitram509.mkat.repository.MediasRepository;

import javax.inject.Inject;
import java.util.List;

class SearchService {

  @Inject
  MediasRepository mediasRepository;
  @Inject
  LanguageResolver languageResolver;

  public SearchResult search(SearchOptions searchOptions) {
    SearchResult searchResult = new SearchResult();
    List<Media> medias = mediasRepository.find(searchOptions);
    map(searchResult, medias);
    return searchResult;
  }

  public SearchResult searchByDiskId(String diskId) {
    SearchResult searchResult = new SearchResult();
    List<Media> medias = mediasRepository.findByDiskId(diskId);
    map(searchResult, medias);
    return searchResult;
  }

  private void map(SearchResult searchResult, List<Media> medias) {
    for (Media media : medias) {
      Film film = new Film();
      film.name = media.name;
      film.id = media.media_id;
      film.description = media.description;
      film.label = media.label;
      film.updated = media.updated;
      film.numberOfFiles = media.numberOfFiles;
      film.languages = languageResolver.getLanguagesFromId(media.languages);
      searchResult.getFilme().add(film);
    }
  }
}