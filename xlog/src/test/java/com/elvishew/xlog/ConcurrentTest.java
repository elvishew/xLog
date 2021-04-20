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

import com.elvishew.xlog.printer.Printer;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.elvishew.xlog.LogLevel.VERBOSE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConcurrentTest {

  @Test
  public void printLogsConcurrently() {

    // 5 printers and 5 containers.

    List<SequencedLog> logsContainer1 = new ArrayList<>();
    List<SequencedLog> logsContainer2 = new ArrayList<>();
    List<SequencedLog> logsContainer3 = new ArrayList<>();
    List<SequencedLog> logsContainer4 = new ArrayList<>();
    List<SequencedLog> logsContainer5 = new ArrayList<>();

    ThreadSafePrinter printer1 = new ThreadSafePrinter(logsContainer1);
    ThreadSafePrinter printer2 = new ThreadSafePrinter(logsContainer2);
    ThreadSafePrinter printer3 = new ThreadSafePrinter(logsContainer3);
    ThreadSafePrinter printer4 = new ThreadSafePrinter(logsContainer4);
    ThreadSafePrinter printer5 = new ThreadSafePrinter(logsContainer5);

    XLog.init(VERBOSE, printer1, printer2, printer3, printer4, printer5);

    // 4 threads print logs concurrently.

    final AtomicBoolean t1Done = new AtomicBoolean(false);
    final AtomicBoolean t2Done = new AtomicBoolean(false);
    final AtomicBoolean t3Done = new AtomicBoolean(false);
    final AtomicBoolean t4Done = new AtomicBoolean(false);

    final int logsCount = 2000;
    new Thread(new Runnable() {
      @Override
      public void run() {
        for (int i = 0; i < logsCount; i++) {
          XLog.i("t1 " + i);
          XLog.i("t1 " + i);
          XLog.i("t1 " + i);
        }
        t1Done.set(true);
      }
    }).start();

    new Thread(new Runnable() {
      @Override
      public void run() {
        for (int j = 0; j < logsCount; j++) {
          XLog.d("t2 " + j);
        }
        t2Done.set(true);
      }
    }).start();

    new Thread(new Runnable() {
      @Override
      public void run() {
        for (int k = 0; k < logsCount; k++) {
          XLog.d("t3 " + k);
          XLog.d("t3 " + k);
        }
        t3Done.set(true);
      }
    }).start();

    new Thread(new Runnable() {
      @Override
      public void run() {
        for (int k = 0; k < logsCount; k++) {
          XLog.d("t4 " + k);
          XLog.d("t4 " + k);
          XLog.d("t4 " + k);
        }
        t4Done.set(true);
      }
    }).start();

    // Wait until done.
    while (!t1Done.get() || !t2Done.get() || !t3Done.get() || !t4Done.get()) ;

    // Assert logs number in all containers.
    assertEquals(logsContainer1.size(), logsContainer2.size());
    assertEquals(logsContainer1.size(), logsContainer3.size());
    assertEquals(logsContainer1.size(), logsContainer4.size());
    assertEquals(logsContainer1.size(), logsContainer5.size());

    // Assert logs content in all containers.
    int size = logsContainer1.size();
    for (int i = 0; i < size; i++) {
      assertLog(logsContainer1.get(i), logsContainer2.get(i));
      assertLog(logsContainer1.get(i), logsContainer3.get(i));
      assertLog(logsContainer1.get(i), logsContainer4.get(i));
      assertLog(logsContainer1.get(i), logsContainer5.get(i));
    }
  }

  private void assertLog(SequencedLog expected, SequencedLog log) {
    assertTrue("Expect " + expected + " but found " + log, expected.seq == log.seq);
  }

  private static class ThreadSafePrinter implements Printer {

    int seq;

    private List<SequencedLog> logsContainers;

    public ThreadSafePrinter(List<SequencedLog> logsContainer) {
      this.logsContainers = logsContainer;
    }

    @Override
    public void println(int logLevel, String tag, String msg) {
      synchronized (this) {
        logsContainers.add(new SequencedLog(seq++, msg));
      }
    }
  }

  private static class SequencedLog {
    int seq;
    String msg;

    SequencedLog(int seq, String msg) {
      this.seq = seq;
      this.msg = msg;
    }

    @Override
    public String toString() {
      return "seq: " + seq + ", msg: " + msg;
    }
  }
}