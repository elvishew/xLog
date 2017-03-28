/*
 * Copyright 2016 Elvis Hew
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.elvishew.xlog;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.elvishew.xlog.XLog.assertInitialization;

/**
 * Utilities for convenience.
 *
 * @since 1.2.0
 */
public class LogUtils {

  /**
   * Format a JSON string using default JSON formatter.
   *
   * @param json the JSON string to format
   * @return the formatted string
   */
  public static String formatJson(String json) {
    assertInitialization();
    return XLog.sLogConfiguration.jsonFormatter.format(json);
  }

  /**
   * Format an XML string using default XML formatter.
   *
   * @param xml the XML string to format
   * @return the formatted string
   */
  public static String formatXml(String xml) {
    assertInitialization();
    return XLog.sLogConfiguration.xmlFormatter.format(xml);
  }

  /**
   * Format a throwable using default throwable formatter.
   *
   * @param throwable the throwable to format
   * @return the formatted string
   */
  public static String formatThrowable(Throwable throwable) {
    assertInitialization();
    return XLog.sLogConfiguration.throwableFormatter.format(throwable);
  }

  /**
   * Format a thread using default thread formatter.
   *
   * @param thread the thread to format
   * @return the formatted string
   */
  public static String formatThread(Thread thread) {
    assertInitialization();
    return XLog.sLogConfiguration.threadFormatter.format(thread);
  }

  /**
   * Format a stack trace using default stack trace formatter.
   *
   * @param stackTrace the stack trace to format
   * @return the formatted string
   */
  public static String formatStackTrace(StackTraceElement[] stackTrace) {
    assertInitialization();
    return XLog.sLogConfiguration.stackTraceFormatter.format(stackTrace);
  }

  /**
   * Add border to string segments using default border formatter.
   *
   * @param segments the string segments to add border to
   * @return the bordered string segments
   */
  public static String addBorder(String[] segments) {
    assertInitialization();
    return XLog.sLogConfiguration.borderFormatter.format(segments);
  }

  /**
   * Compress all files under the specific folder to a single zip file.
   * <p>
   * Should be call in background thread.
   *
   * @param folderPath  the specific folder path
   * @param zipFilePath the zip file path
   * @throws IOException if any error occurs
   * @since 2.14.0
   */
  public static void compress(String folderPath, String zipFilePath) throws IOException {
    File folder = new File(folderPath);
    if (!folder.exists() || !folder.isDirectory()) {
      throw new IOException("Folder " + folderPath + " does't exist or isn't a directory");
    }

    File zipFile = new File(zipFilePath);
    if (!zipFile.exists()) {
      File zipFolder = zipFile.getParentFile();
      if (!zipFolder.exists()) {
        if (!zipFolder.mkdirs()) {
          throw new IOException("Zip folder " + zipFolder.getAbsolutePath() + " not created");
        }
      }
      if (!zipFile.createNewFile()) {
        throw new IOException("Zip file " + zipFilePath + " not created");
      }
    }

    BufferedInputStream bis;
    ZipOutputStream zos = new ZipOutputStream(
        new BufferedOutputStream(new FileOutputStream(zipFile)));
    try {
      final int BUFFER_SIZE = 8 * 1024; // 8K
      byte buffer[] = new byte[BUFFER_SIZE];
      for (String fileName : folder.list()) {
        if (fileName.equals(".") || fileName.equals("..")) {
          continue;
        }

        File file = new File(folder, fileName);
        if (!file.isFile()) {
          continue;
        }

        FileInputStream fis = new FileInputStream(file);
        bis = new BufferedInputStream(fis, BUFFER_SIZE);
        try {
          ZipEntry entry = new ZipEntry(fileName);
          zos.putNextEntry(entry);
          int count;
          while ((count = bis.read(buffer, 0, BUFFER_SIZE)) != -1) {
            zos.write(buffer, 0, count);
          }
        } finally {
          try {
            bis.close();
          } catch (IOException e) {
            // Ignore
          }
        }
      }
    } finally {
      try {
        zos.close();
      } catch (IOException e) {
        // Ignore
      }
    }
  }
}
