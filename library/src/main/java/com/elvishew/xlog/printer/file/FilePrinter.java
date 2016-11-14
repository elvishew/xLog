/*
 * Copyright 2015 Elvis Hew
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

package com.elvishew.xlog.printer.file;

import com.elvishew.xlog.DefaultsFactory;
import com.elvishew.xlog.printer.Printer;
import com.elvishew.xlog.printer.file.backup.BackupStrategy;
import com.elvishew.xlog.printer.file.naming.FileNameGenerator;
import com.elvishew.xlog.printer.flattener.LogFlattener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Log {@link Printer} using file system. When print a log, it will print it to the specified file.
 * <p>
 * Use the {@link Builder} to construct a {@link FilePrinter} object.
 */
public class FilePrinter implements Printer {

    /**
     * The folder path of log file.
     */
    private final String folderPath;

    /**
     * The file name generator for log file.
     */
    private final FileNameGenerator fileNameGenerator;

    /**
     * The backup strategy for log file.
     */
    private final BackupStrategy backupStrategy;

    /**
     * The log flattener when print a log.
     */
    private LogFlattener logFlattener;

    /**
     * Log writer.
     */
    private BufferedWriter mBufferedWriter;

    /**
     * The file name of last used log file.
     */
    private String lastFileName;

    /**
     * The currently using log file.
     */
    private File logFile;

    /*package*/ FilePrinter(Builder builder) {
        folderPath = builder.folderPath;
        fileNameGenerator = builder.fileNameGenerator;
        backupStrategy = builder.backupStrategy;
        logFlattener = builder.logFlattener;

        checkLogFolder();
    }

    @Override
    public void println(int logLevel, String tag, String msg) {
        if (lastFileName == null || fileNameGenerator.isFileNameChangeable()) {
            String newFileName = fileNameGenerator.generateFileName(logLevel,
                    System.currentTimeMillis());
            if (newFileName == null || newFileName.trim().length() == 0) {
                throw new IllegalArgumentException("File name should not be empty.");
            }
            if (!newFileName.equals(lastFileName)) {
                if (mBufferedWriter != null) {
                    closeLogWriter();
                }
                lastFileName = newFileName;
                openLogWriter();
            }
        }

        // If some how the writer is not opened, just give up logging.
        if (mBufferedWriter == null) {
            return;
        }

        if (backupStrategy.shouldBackup(logFile)) {
            // Backup the log file, and create a new log file.
            closeLogWriter();
            File backupFile = new File(folderPath, lastFileName + ".bak");
            if (backupFile.exists()) {
                backupFile.delete();
            }
            logFile.renameTo(backupFile);
            openLogWriter();
            if (mBufferedWriter == null) {
                return;
            }
        }
        try {
            String flattenedLog = logFlattener.flatten(logLevel, tag, msg).toString();
            mBufferedWriter.write(flattenedLog);
            mBufferedWriter.newLine();
            mBufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Make sure the folder of log file exists.
     */
    private void checkLogFolder() {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    private void openLogWriter() {
        logFile = new File(folderPath, lastFileName);
        if (!logFile.exists()) {
            try {
                File parent = logFile.getParentFile();
                if (!parent.exists()) {
                    parent.mkdirs();
                }
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                lastFileName = null;
                logFile = null;
                return;
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(logFile, true);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            mBufferedWriter = new BufferedWriter(osw);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            lastFileName = null;
            logFile = null;
        }
    }

    private void closeLogWriter() {
        try {
            if (mBufferedWriter != null) {
                mBufferedWriter.close();
                mBufferedWriter = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Builder for {@link FilePrinter}.
     */
    public static class Builder {

        /**
         * The folder path of log file.
         */
        String folderPath;

        /**
         * The file name generator for log file.
         */
        FileNameGenerator fileNameGenerator;

        /**
         * The backup strategy for log file.
         */
        BackupStrategy backupStrategy;

        /**
         * The log flattener when print a log.
         */
        LogFlattener logFlattener;

        /**
         * Construct a builder.
         *
         * @param folderPath the folder path of log file
         */
        public Builder(String folderPath) {
            this.folderPath = folderPath;
        }

        /**
         * Set the file name generator for log file.
         *
         * @param fileNameGenerator the file name generator for log file
         * @return the builder
         */
        public Builder fileNameGenerator(FileNameGenerator fileNameGenerator) {
            this.fileNameGenerator = fileNameGenerator;
            return this;
        }

        /**
         * Set the backup strategy for log file.
         *
         * @param backupStrategy the backup strategy for log file
         * @return the builder
         */
        public Builder backupStrategy(BackupStrategy backupStrategy) {
            this.backupStrategy = backupStrategy;
            return this;
        }

        /**
         * Set the log flattener when print a log.
         *
         * @param logFlattener the log flattener when print a log
         * @return the builder
         */
        public Builder logFormatter(LogFlattener logFlattener) {
            this.logFlattener = logFlattener;
            return this;
        }

        /**
         * Build configured {@link FilePrinter} object.
         *
         * @return the built configured {@link FilePrinter} object
         */
        public FilePrinter build() {
            fillEmptyFields();
            return new FilePrinter(this);
        }

        private void fillEmptyFields() {
            if (fileNameGenerator == null) {
                fileNameGenerator = DefaultsFactory.createFileNameGenerator();
            }
            if (backupStrategy == null) {
                backupStrategy = DefaultsFactory.createBackupStrategy();
            }
            if (logFlattener == null) {
                logFlattener = DefaultsFactory.createLogFlattener();
            }
        }
    }
}
