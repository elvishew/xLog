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

package com.elvishew.xlog.printer;

/**
 * Represents a group of Printers that should used to print logs in the same time, each printer
 * may probably print the log to different place.
 */
public class PrinterSet implements Printer {

  private Printer[] printers;

  /**
   * Constructor, pass printers in and will use all these printers to print the same logs.
   *
   * @param printers the printers used to print the same logs
   */
  public PrinterSet(Printer... printers) {
    this.printers = printers;
  }

  @Override
  public void println(int logLevel, String tag, String msg) {
    for (Printer printer : printers) {
      printer.println(logLevel, tag, msg);
    }
  }
}
