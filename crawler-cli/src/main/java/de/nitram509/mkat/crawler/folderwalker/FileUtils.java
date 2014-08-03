package de.nitram509.mkat.crawler.folderwalker;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileUtils {

  static File[] getFiles(String folder, boolean includeHidden) {
    File f = new File(folder);
    File[] files = f.listFiles(new FilesOnlyFilter(includeHidden));
    return files == null ? new File[0] : files;
  }

  static File[] getFolders(String folder, boolean includeHidden) {
    File f = new File(folder);
    File[] folders = f.listFiles(new DirectoriesOnlyFilter(includeHidden));
    return folders == null ? new File[0] : folders;
  }

  static class FilesOnlyFilter implements FileFilter {

    private final boolean includeHidden;

    FilesOnlyFilter(boolean includeHidden) {
      this.includeHidden = includeHidden;
    }

    @Override
    public boolean accept(File file) {
      return file.isFile() && (!file.isHidden() || (includeHidden && file.isHidden()));
    }
  }

  static class DirectoriesOnlyFilter implements FileFilter {

    private final boolean includeHidden;

    DirectoriesOnlyFilter(boolean includeHidden) {
      this.includeHidden = includeHidden;
    }

    @Override
    public boolean accept(File file) {
      return file.isDirectory() && (!file.isHidden() || (includeHidden && file.isHidden()));
    }
  }

  public static String computeMd5(File file) throws NoSuchAlgorithmException, IOException {
    MessageDigest md5 = MessageDigest.getInstance("MD5");
    RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
    FileChannel channel = randomAccessFile.getChannel();
    ByteBuffer buf64MB = ByteBuffer.allocate(64 * 1000 * 1000);
    while (channel.read(buf64MB) > 0) {
      buf64MB.limit(buf64MB.position());
      buf64MB.position(0);
      md5.update(buf64MB);
      buf64MB.clear();
    }
    byte[] digest = md5.digest();
    StringBuilder sb = new StringBuilder();
    for (byte b : digest) {
      String hex = Integer.toHexString(b & 0x000000FF);
      if (hex.length() == 1) {
        sb.append("0");
      }
      sb.append(hex);
    }
    return sb.toString();
  }

  public static String getExtensionWithDot(File file) {
    String name = file.getName();
    int i = name.lastIndexOf('.');
    return name.substring(i > -1 ? i : 0);
  }

}
