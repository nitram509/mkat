package de.nitram509.mkat.language;

import de.nitram509.mkat.api.languages.Language;
import de.nitram509.videomedialist.repository.LanguageService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class LanguageResolver {

  @Inject
  LanguageService languageService;

  private List<Language> languages = new ArrayList<>(16);

  public List<Language> getLanguagesFromId(int id) {
    if (languages.size() == 0) {
      languages.addAll(languageService.loadLanguages());
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
