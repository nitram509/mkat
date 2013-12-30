package de.nitram509.mkat.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement(name = "File")
public class MkatFile {

  @XmlElement(name = "id")
  public long id = 0;

  @XmlElement(name = "media_id")
  public long media_id;

  @XmlElement(name = "name")
  public String name;

  @XmlElement(name = "size")
  public long size;

  @XmlElement(name = "datetime")
  public Date datetime;

  @XmlElement(name = "notizen")
  public String notizen;

  @XmlElement(name = "isdir")
  public boolean isdir;

  @XmlElement(name = "md5")
  public String md5;
}