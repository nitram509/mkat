package de.nitram509.mkat.api.diskcontent;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement(name = "FileInfo")
public class FileInfo {

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
