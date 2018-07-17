package com.elvishew.xlog.printer.file.clean;

import java.io.File;

/**
 * Limit the file life of a max time.
 */
public class FileLastModifiedCleanStrategy implements CleanStrategy {

  private long maxTimeMillis;

  /**
   * Constructor.
   *
   * @param maxTimeMillis the max time the file can keep
   */
  public FileLastModifiedCleanStrategy(long maxTimeMillis) {
    this.maxTimeMillis = maxTimeMillis;
  }

  @Override
  public boolean shouldClean(File file) {
    long currentTimeMillis = System.currentTimeMillis();
    long lastModified = file.lastModified();
    return (currentTimeMillis - lastModified > maxTimeMillis);
  }
}
