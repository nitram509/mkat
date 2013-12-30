package de.nitram509.mkat.language;

import de.nitram509.mkat.api.languages.Language;
import de.nitram509.videomedialist.repository.LanguageService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LanguageResolverTest {

  private LanguageResolver resolver;

  private Language lang_de = createLanguage("de", "deutsch");
  private Language lang_en = createLanguage("en", "englisch");
  private Language lang_fr = createLanguage("fr", "französisch");

  @BeforeMethod
  public void setup() {
    resolver = new LanguageResolver();
    resolver.languageService = mock(LanguageService.class);
    when(resolver.languageService.loadLanguages()).thenReturn(createLanguages());
  }

  @Test
  public void resolver_empty_list() {
    List<Language> actual = resolver.getLanguagesFromId(0);
    assertThat(actual).isNotNull();
  }

  @Test
  public void resolver_one_element() {
    List<Language> actual;

    actual = resolver.getLanguagesFromId(1);
    assertThat(actual).containsOnly(lang_de);

    actual = resolver.getLanguagesFromId(2);
    assertThat(actual).containsOnly(lang_en);

    actual = resolver.getLanguagesFromId(4);
    assertThat(actual).containsOnly(lang_fr);
  }

  private List<Language> createLanguages() {
    List<Language> result = new ArrayList<>();
    result.add(lang_de);
    result.add(lang_en);
    result.add(lang_fr);
    return result;
  }

  private Language createLanguage(String de, String deutsch) {
    Language language = new Language();
    language.name = de;
    language.shortName = deutsch;
    return language;
  }
}
