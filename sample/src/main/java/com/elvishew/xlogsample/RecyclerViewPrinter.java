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

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.printer.Printer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Display logs in a {@link android.support.v7.widget.RecyclerView}.
 */
public class RecyclerViewPrinter implements Printer {

  private RecyclerView recyclerView;

  private LogAdapter adapter;

  public RecyclerViewPrinter(RecyclerView recyclerView) {
    // Setup the recycler view.
    adapter = new LogAdapter(LayoutInflater.from(recyclerView.getContext()));
    LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(adapter);

    this.recyclerView = recyclerView;
  }

  @Override
  public void println(int logLevel, String tag, String msg) {
    // Append the log the the recycler view.
    adapter.addLog(new LogItem(System.currentTimeMillis(), logLevel, tag, msg));

    // Scroll to the bottom so we can see the newly-printed log.
    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
  }

  private static class LogItem {

    private static SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss.SSS", Locale.US);

    long timestamp;
    int logLevel;
    String tag;
    String msg;

    private String label;

    LogItem(long timestamp, int logLevel, String tag, String msg) {
      this.timestamp = timestamp;
      this.logLevel = logLevel;
      this.tag = tag;
      this.msg = msg;
    }

    /**
     * Get the label, with formatted time, log level and tag.
     */
    String getLabel() {
      // Lazily concat the label.
      if (label == null) {
        label = timeFormat.format(new Date(timestamp))
            + " "
            + LogLevel.getShortLevelName(logLevel)
            + "/"
            + tag
            + ": ";
      }
      return label;
    }
  }

  private static class LogAdapter extends RecyclerView.Adapter<LogViewHolder> {

    private LayoutInflater inflater;

    private List<LogItem> logs = new ArrayList<>();

    LogAdapter(LayoutInflater inflater) {
      this.inflater = inflater;
    }

    void addLog(LogItem logItem) {
      logs.add(logItem);
      notifyItemInserted(logs.size() - 1);
    }

    @Override
    public LogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View itemView = inflater.inflate(R.layout.item_log, parent, false);
      return new LogViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LogViewHolder holder, int position) {
      LogItem logItem = logs.get(position);

      // Set color according to different log level.
      int color = getHighlightColor(logItem.logLevel);
      holder.labelView.setTextColor(color);
      holder.messageView.setTextColor(color);

      // Display label and message.
      holder.labelView.setText(logItem.getLabel());
      holder.messageView.setText(logItem.msg);
    }

    /**
     * Get the highlight color for specific log level.
     *
     * @param logLevel the specific log level
     * @return the highlight color
     */
    private int getHighlightColor(int logLevel) {
      int hightlightColor;
      switch (logLevel) {
        case LogLevel.VERBOSE:
          hightlightColor = 0xffbbbbbb;
          break;
        case LogLevel.DEBUG:
          hightlightColor = 0xffffffff;
          break;
        case LogLevel.INFO:
          hightlightColor = 0xff6a8759;
          break;
        case LogLevel.WARN:
          hightlightColor = 0xffbbb529;
          break;
        case LogLevel.ERROR:
          hightlightColor = 0xffff6b68;
          break;
        default:
          hightlightColor = 0xffffff00;
          break;
      }
      return hightlightColor;
    }

    @Override
    public int getItemCount() {
      return logs.size();
    }
  }

  private static class LogViewHolder extends RecyclerView.ViewHolder {

    TextView labelView;
    TextView messageView;

    LogViewHolder(View itemView) {
      super(itemView);
      labelView = (TextView) itemView.findViewById(R.id.label);
      messageView = (TextView) itemView.findViewById(R.id.message);
    }
  }
}
