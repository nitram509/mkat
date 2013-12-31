package de.nitram509.mkat;

import de.nitram509.mkat.api.diskcontent.DiskContent;
import de.nitram509.mkat.api.diskcontent.FileInfo;
import de.nitram509.mkat.model.MkatFile;
import de.nitram509.mkat.repository.FilesRepository;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DiskContentService {

  @Inject
  FilesRepository filesRepository;

  public DiskContent search(String diskId) {
    DiskContent diskContent = new DiskContent();
    List<MkatFile> mkatFiles = filesRepository.find(Long.parseLong(diskId));
    for (MkatFile mkatFile : mkatFiles) {
      FileInfo fileInfo = new FileInfo();
      fileInfo.name = mkatFile.name;
      fileInfo.size = mkatFile.size;
      fileInfo.datetime = mkatFile.datetime;
      fileInfo.md5 = mkatFile.md5;
      fileInfo.isdir = mkatFile.isdir;
      fileInfo.notizen = mkatFile.notizen;
      diskContent.files.add(fileInfo);
    }
    return diskContent;
  }

}
