package com.elvishew.xlog.printer.file.clean;

import java.io.File;

/**
 * Limit the file life of a max time.
 */
public class TimeCleanStrategy implements CleanStrategy {

  private long maxTimeMillis;

  /**
   * Constructor.
   *
   * @param maxTimeMillis the max time the file can keep
   */
  public TimeCleanStrategy(long maxTimeMillis) {
    this.maxTimeMillis = maxTimeMillis;
  }

  @Override
  public boolean shouldClean(File file) {
    long currentTimeMillis = System.currentTimeMillis();
    long fileTimeMillis = file.lastModified();
    return (Math.abs(currentTimeMillis - fileTimeMillis) > maxTimeMillis);
  }
}
