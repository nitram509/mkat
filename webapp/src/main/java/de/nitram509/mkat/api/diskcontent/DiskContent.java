package de.nitram509.mkat.api.diskcontent;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DiskContent {

  @XmlElement(name = "diskId")
  public int diskId;

  @XmlElement(name = "diskName")
  public String diskName;

  @XmlElement(name = "files")
  public final List<FileInfo> files = new ArrayList<>();

  @XmlElement(name = "filesLength")
  public int getFilesLength() {
    return files.size();
  }

}
