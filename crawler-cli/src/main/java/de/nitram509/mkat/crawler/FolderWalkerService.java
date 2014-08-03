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
import de.nitram509.mkat.model.Poster;
import de.nitram509.mkat.repository.FilesRepository;
import de.nitram509.mkat.repository.MediasRepository;
import de.nitram509.mkat.repository.MoviesRepository;
import de.nitram509.mkat.repository.PostersRepository;
import jedi.functional.Filter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
  private PostersRepository postersRepository;

  public void setup(CrawlerConfig crawlerConfig) {
    Injector injector = Guice.createInjector(
        new MkatAppModule()
    );
    mediasRepository = injector.getInstance(MediasRepository.class);
    moviesRepository = injector.getInstance(MoviesRepository.class);
    filesRepository = injector.getInstance(FilesRepository.class);
    postersRepository = injector.getInstance(PostersRepository.class);

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
      if (crawlerConfig.isUpdatePoster()) {
        renamePosterFilesAndSaveToDatabase(discoveryResult.media, discoveryResult.posters);
      } else {
        deletePosterFile(discoveryResult);
      }
    }

  }

  private void deletePosterFile(DiscoveryResult discoveryResult) {
    for (File poster : discoveryResult.posters) {
      log.warning("Deleting poster file " + poster.getName());
      poster.delete();
    }
  }

  private void renamePosterFilesAndSaveToDatabase(Media media, File[] posters) {
    if (media.media_id != 0) {
      String name = fixedLength(media.name, 100);
      String label = fixedLength(media.label, 50);
      List<Media> medias = mediasRepository.findByNameAndLabel(name, label);
      if (medias.size() == 0) {
        log.info(String.format("Nothing found found for name=%s and label=%s", name, label));
      } else if (medias.size() == 1) {
        media = medias.get(0);
      } else {
        log.info(String.format("Duplicates found for name=%s and label=%s", name, label));
      }
    }
    if (media.media_id != 0) {
      for (File poster : posters) {
        File newPosterName = createPosterName(media.media_id, poster);
        poster.renameTo(newPosterName);
        postersRepository.save(new Poster(newPosterName.getName(), media.media_id));
      }
    }
  }

  private String fixedLength(String value, int maxLength) {
    if (value == null) {
      return null;
    }
    return value.substring(0, Math.min(value.length(), maxLength));
  }

  private void dryRunPrintStdOut(DiscoveryResult discoveryResult) {
    log.warning("--- DRY RUN (please use a GID to avoid that) ---------------------------------");
    for (MkatFile file : discoveryResult.files) {
      log.warning(String.format("%s (%s) (%s)", file.name, file.isdir ? "d" : Long.toString(file.size), file.md5));
    }
    log.warning("--- Done. ---------------------------------");
  }

  private void saveToDatabase(DiscoveryResult discoveryResult) {
    log.info("Saving to database ...");
    moviesRepository.insert(discoveryResult.movie);

    discoveryResult.media.movie_id = discoveryResult.movie.movie_id;
    mediasRepository.save(discoveryResult.media);

    for (MkatFile file : discoveryResult.files) {
      file.media_id = discoveryResult.media.media_id;
      filesRepository.save(file);
    }
  }

  private File createPosterName(int id, File posterFile) {
    File mediaPosterPath = new File(crawlerConfig.getPosterDataPath(), "" + id);
    if (!mediaPosterPath.exists()) {
      mediaPosterPath.mkdir();
    }
    if (mediaPosterPath.exists() && mediaPosterPath.isDirectory()) {
      return new File(mediaPosterPath, posterFile.getName());
    }
    throw new RuntimeException("Couldn't create target path " + mediaPosterPath.toString());
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
      computeMd5Sum(folderInfo, mkatFile);
      copyPosterImage(folderInfo, discoveryResult);
      discoveryResult.add(mkatFile);
    }

    media.size = fold(0L, asList(discoveryResult.files), (currie, file) -> currie + file.size);
    Date lastUpdated = fold(new Date(1L), asList(discoveryResult.files), (currie, mkatFile) -> currie.after(mkatFile.datetime) ? currie : mkatFile.datetime);
    media.updated = lastUpdated;
    movie.updated = lastUpdated;

    discoveryResult.media = media;
    discoveryResult.movie = movie;
    return discoveryResult;
  }

  private void copyPosterImage(FolderInfo folderInfo, DiscoveryResult discoveryResult) {
    if (folderInfo.isPoster()) {
      File targetFile = new File(crawlerConfig.getPosterDataPath(), "img-" + System.currentTimeMillis() + FileUtils.getExtensionWithDot(folderInfo.asFile()));
      log.info(String.format("Copy poster %s --> %s ", folderInfo.getName(), targetFile.getName()));
      try {
        Files.copy(folderInfo.asFile().toPath(), targetFile.toPath());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      discoveryResult.addPoster(targetFile);
    }
  }

  private void computeMd5Sum(FolderInfo folderInfo, MkatFile mkatFile) throws IOException {
    if (folderInfo.isFile() && crawlerConfig.isMd5()) {
      try {
        mkatFile.md5 = FileUtils.computeMd5(folderInfo.asFile());
      } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
      }
    }
  }


}
