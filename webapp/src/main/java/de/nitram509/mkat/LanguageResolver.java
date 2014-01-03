package de.nitram509.mkat;

import de.nitram509.mkat.api.languages.Language;
import de.nitram509.mkat.repository.LanguageRepository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

class LanguageResolver {

  @Inject
  LanguageRepository languageService;

  private List<Language> languages = new ArrayList<>(16);

  public List<Language> getLanguagesFromId(int id) {
    if (languages.size() == 0) {
      languages.addAll(languageService.findAll());
    }
    List<Language> result = new ArrayList<>();
    int powerOfTwo = 1;
    for (Language language : languages) {
      if ((id & powerOfTwo) == powerOfTwo) {
        result.add(language);
      }
      powerOfTwo <<= 1;
    }
    return result;
  }
}
