package de.nitram509.videomedialist.repository;

import de.nitram509.mkat.api.diskcontent.DiskContent;
import de.nitram509.videomedialist.repository.connection.VideoMediaListConnectionFactory;
import de.nitram509.videomedialist.repository.DiskContentService;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static org.testng.Assert.assertNotNull;

public class DiskContentServiceLearningTest {

  private DiskContentService diskContentService;

  @BeforeMethod
  public void setup() {
    diskContentService = new DiskContentService();
    diskContentService.connection = new VideoMediaListConnectionFactory().get();
  }

  @AfterClass
  public void afterMethod() throws SQLException {
    diskContentService.connection.close();
  }

  @Test
  public void do_actual_simple_search_on_database() {
    DiskContent diskContent = diskContentService.search("31635");
    assertNotNull(diskContent);
  }


}
