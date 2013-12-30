package de.nitram509.videomedialist.repository;

import de.nitram509.mkat.api.languages.Language;
import de.nitram509.videomedialist.repository.connection.VideoMediaListConnectionFactory;
import de.nitram509.videomedialist.repository.LanguageService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class LanguageServiceLearningTest {

  private LanguageService languageService;

  @BeforeMethod
  public void setup() {
    languageService = new LanguageService();
    languageService.connection = new VideoMediaListConnectionFactory().get();
  }

  @Test
  public void loadLanguages() {
    List<Language> actual = languageService.loadLanguages();

    assertThat(actual).contains(createLang("deutsch", "de", "l_de.png"));
    assertThat(actual).contains(createLang("französisch", "fr", "l_fr.png"));
  }

  private Language createLang(String name, String shortName, String image) {
    Language lang = new Language();
    lang.name = name;
    lang.shortName = shortName;
    lang.image = image;
    return lang;
  }
}
