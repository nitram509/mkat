package de.nitram509.mkat.repository;

import de.nitram509.mkat.api.languages.Language;
import de.nitram509.mkat.repository.connection.MkatConnectionFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class LanguageRepositoryLearningTest {

  private LanguageRepository languageRepository;

  @BeforeMethod
  public void setup() {
    languageRepository = new LanguageRepository();
    languageRepository.setConnection(new MkatConnectionFactory().get());
  }

  @Test
  public void loadLanguages() {
    List<Language> actual = languageRepository.findAll();

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
