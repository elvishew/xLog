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

package com.elvishew.xlogsample;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.Logger;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.printer.AndroidPrinter;
import com.elvishew.xlog.printer.Printer;

public class MainActivity extends AppCompatActivity {

  private static final String MESSAGE = "Simple message";

  private static final int[] LEVELS = new int[]{LogLevel.VERBOSE, LogLevel.DEBUG, LogLevel.INFO,
      LogLevel.WARN, LogLevel.ERROR};

  private static final int[] STACK_TRACE_DEPTHS = new int[]{0, 1, 2, 3, 4, 5};

  private TextView tagView;
  private Spinner levelView;
  private CheckedTextView threadInfo;
  private CheckedTextView stackTraceInfo;
  private View stackTraceDepthContainer;
  private Spinner stackTraceDepth;
  private CheckedTextView border;

  private FloatingActionButton print;

  private Printer viewPrinter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    tagView = (TextView) findViewById(R.id.tag);
    tagView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showChangeTagDialog();
      }
    });

    levelView = (Spinner) findViewById(R.id.level);

    threadInfo = (CheckedTextView) findViewById(R.id.thread_info);
    threadInfo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        threadInfo.toggle();
      }
    });

    stackTraceInfo = (CheckedTextView) findViewById(R.id.stack_trace_info);
    stackTraceInfo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        stackTraceInfo.toggle();
        setEnabledStateOnViews(stackTraceDepthContainer, stackTraceInfo.isChecked());
      }
    });
    stackTraceDepthContainer = findViewById(R.id.stack_trace_depth_container);
    setEnabledStateOnViews(stackTraceDepthContainer, false);
    stackTraceDepth = (Spinner) stackTraceDepthContainer.findViewById(R.id.stack_trace_depth);

    border = (CheckedTextView) findViewById(R.id.border);
    border.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        border.toggle();
      }
    });

    print = (FloatingActionButton) findViewById(R.id.print);
    print.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        printLog();
      }
    });

    RecyclerView logContainer = (RecyclerView) findViewById(R.id.log_container);
    viewPrinter = new RecyclerViewPrinter(logContainer);

    XLog.printers(viewPrinter).i("XLog is ready.\nPrint your log now!");
  }

  /**
   * Show a dialog for user to change the tag, empty text is not allowed.
   */
  private void showChangeTagDialog() {
    View view = getLayoutInflater().inflate(R.layout.dialog_change_tag, null, false);
    final EditText tagEditText = (EditText) view.findViewById(R.id.tag);
    tagEditText.setText(tagView.getText());

    new AlertDialog.Builder(this)
        .setTitle(R.string.change_tag)
        .setView(view)
        .setNegativeButton(android.R.string.cancel, null)
        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            String tag = tagEditText.getText().toString().trim();
            if (!tag.isEmpty()) {
              tagView.setText(tag);
            }
          }
        })
        .show();
  }

  /**
   * Makes sure the view (and any children) get the enabled state changed.
   */
  public static void setEnabledStateOnViews(View v, boolean enabled) {
    v.setEnabled(enabled);

    if (v instanceof ViewGroup) {
      final ViewGroup vg = (ViewGroup) v;
      for (int i = vg.getChildCount() - 1; i >= 0; i--) {
        setEnabledStateOnViews(vg.getChildAt(i), enabled);
      }
    }
  }

  /**
   * Print the configured log.
   */
  private void printLog() {
    Logger.Builder builder = new Logger.Builder();

    String tag = tagView.getText().toString();
    if (!TextUtils.isEmpty(tag)) {
      builder.tag(tag);
    }

    if (threadInfo.isChecked()) {
      builder.t();
    } else {
      builder.nt();
    }

    if (stackTraceInfo.isChecked()) {
      builder.st(STACK_TRACE_DEPTHS[stackTraceDepth.getSelectedItemPosition()]);
    } else {
      builder.nst();
    }

    if (border.isChecked()) {
      builder.b();
    } else {
      builder.nb();
    }

    // Print the log to view, logcat and file.
    builder.printers(
        viewPrinter,
        new AndroidPrinter(),
        XLogSampleApplication.globalFilePrinter);

    Logger logger = builder.build();

    int levelPosition = levelView.getSelectedItemPosition();
    int level = LEVELS[levelPosition];
    switch (level) {
      case LogLevel.VERBOSE:
        logger.v(MESSAGE);
        break;
      case LogLevel.DEBUG:
        logger.d(MESSAGE);
        break;
      case LogLevel.INFO:
        logger.i(MESSAGE);
        break;
      case LogLevel.WARN:
        logger.w(MESSAGE);
        break;
      case LogLevel.ERROR:
        logger.e(MESSAGE);
        break;
    }
  }
}