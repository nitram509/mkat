package de.nitram509.mkat.crawler;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.nitram509.mkat.MkatAppModule;
import de.nitram509.mkat.crawler.folderwalker.FileUtils;
import de.nitram509.mkat.crawler.folderwalker.FolderInfo;
import de.nitram509.mkat.crawler.folderwalker.FolderWalker;
import de.nitram509.mkat.model.Media;
import de.nitram509.mkat.model.MkatFile;
import de.nitram509.mkat.model.Movie;
import de.nitram509.mkat.repository.mkat.FilesRepository;
import de.nitram509.mkat.repository.mkat.MediasRepository;
import de.nitram509.mkat.repository.mkat.MoviesRepository;
import jedi.functional.Filter;
import jedi.functional.Functor2;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Arrays.asList;
import static jedi.functional.FunctionalPrimitives.fold;
import static jedi.functional.FunctionalPrimitives.select;

public class FolderWalkerService {

  private Logger log = Logger.getLogger("FolderWalker");

  public static final int MEDIA_TYPE_FOLDER = 64;

  private MediasRepository mediasRepository;
  private MoviesRepository moviesRepository;
  private FilesRepository filesRepository;
  private CrawlerConfig crawlerConfig;

  public void setup(CrawlerConfig crawlerConfig) {
    Injector injector = Guice.createInjector(
        new MkatAppModule()
    );
    mediasRepository = injector.getInstance(MediasRepository.class);
    moviesRepository = injector.getInstance(MoviesRepository.class);
    filesRepository = injector.getInstance(FilesRepository.class);

    if (crawlerConfig.isVerbose()) {
      log.setLevel(Level.INFO);
    } else {
      log.setLevel(Level.WARNING);
    }

    this.crawlerConfig = crawlerConfig;
  }

  public void startWalking() throws IOException {
    Enumeration<FolderInfo> walker = new de.nitram509.mkat.crawler.folderwalker.FolderWalker(crawlerConfig, crawlerConfig.getBaseFolder()).walker();

    log.info("Start walking ... " + crawlerConfig.getBaseFolder());

    List<FolderInfo> baseFolders = select(Collections.list(walker), new Filter<FolderInfo>() {
      @Override
      public Boolean execute(FolderInfo value) {
        return value.isDirectory();
      }
    });

    for (FolderInfo baseFolder : baseFolders) {
      DiscoveryResult discoveryResult = discoverMediaAsFolder(baseFolder);
      if (crawlerConfig.isDryRun()) {
        dryRunPrintStdOut(discoveryResult);
      } else {
        saveToDatabase(discoveryResult);
      }
    }

  }

  private void dryRunPrintStdOut(DiscoveryResult discoveryResult) {
    log.warning("--- DRY RUN (please use a GID to avoid that) ---------------------------------");
    for (MkatFile file : discoveryResult.files) {
      String msg = String.format("%s (%s) (%s)", file.name, file.isdir ? "d" : Long.toString(file.size), file.md5);
      log.warning(msg);
    }
    log.warning("--- Done. ---------------------------------");
  }

  private void saveToDatabase(DiscoveryResult discoveryResult) {
    log.info("Saving to database ...");
    moviesRepository.save(discoveryResult.movie);

    discoveryResult.media.movie_id = discoveryResult.movie.movie_id;
    mediasRepository.save(discoveryResult.media);

    for (MkatFile file : discoveryResult.files) {
      file.media_id = discoveryResult.media.media_id;
      filesRepository.save(file);
    }
  }

  private DiscoveryResult discoverMediaAsFolder(FolderInfo baseFolder) throws IOException {
    Enumeration<FolderInfo> walker = new FolderWalker(crawlerConfig, baseFolder.asFile().getAbsolutePath()).recursiveWalker();

    log.info("Found new Media ... " + baseFolder.getName());

    Media media = new Media();
    media.name = baseFolder.getName();
    media.label = baseFolder.getName();
    media.group_id = crawlerConfig.getGroupId();
    media.media_type = MEDIA_TYPE_FOLDER;

    Movie movie = new Movie();
    movie.name = baseFolder.getName().replaceAll("[.]", " ");
    movie.format = media.media_type;

    DiscoveryResult discoveryResult = new DiscoveryResult();
    while (walker.hasMoreElements()) {
      FolderInfo folderInfo = walker.nextElement();

      MkatFile mkatFile = new MkatFile();
      mkatFile.name = folderInfo.getName();
      mkatFile.size = folderInfo.size();
      mkatFile.isdir = folderInfo.isDirectory();
      mkatFile.datetime = folderInfo.getLastModifiedTime();
      if (folderInfo.isFile() && crawlerConfig.isMd5()) {
        try {
          mkatFile.md5 = FileUtils.computeMd5(folderInfo.asFile());
        } catch (NoSuchAlgorithmException e) {
          throw new RuntimeException(e);
        }
      }
      discoveryResult.add(mkatFile);
    }

    media.size = fold(0L, asList(discoveryResult.files), new Functor2<Long, MkatFile, Long>() {
      @Override
      public Long execute(Long currie, MkatFile file) {
        return currie + file.size;
      }
    });
    Date lastUpdated = fold(new Date(1L), asList(discoveryResult.files), new Functor2<Date, MkatFile, Date>() {
      @Override
      public Date execute(Date currie, MkatFile mkatFile) {
        return currie.after(mkatFile.datetime) ? currie : mkatFile.datetime;
      }
    });
    media.updated = lastUpdated;
    movie.updated = lastUpdated;

    discoveryResult.media = media;
    discoveryResult.movie = movie;
    return discoveryResult;
  }


}
