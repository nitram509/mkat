package de.nitram509.mkat.api.languages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Language {

  @XmlElement(name = "name")
  public String name;

  @XmlElement(name = "shortName")
  public String shortName;

  @XmlElement(name = "image")
  public String image;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Language language = (Language) o;

    if (image != null ? !image.equals(language.image) : language.image != null) return false;
    if (name != null ? !name.equals(language.name) : language.name != null) return false;
    if (shortName != null ? !shortName.equals(language.shortName) : language.shortName != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (shortName != null ? shortName.hashCode() : 0);
    result = 31 * result + (image != null ? image.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Language{" +
            "name='" + name + '\'' +
            ", shortName='" + shortName + '\'' +
            ", image='" + image + '\'' +
            '}';
  }
}
