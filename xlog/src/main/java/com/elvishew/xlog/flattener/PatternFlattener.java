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

package com.elvishew.xlog.flattener;

import com.elvishew.xlog.LogLevel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Flatten the log using the format specified by pattern.
 * <p>
 * Supported parameters:
 * <blockquote>
 * <table border=0 cellspacing=3 cellpadding=0 summary="Supported parameters">
 *   <tr bgcolor="#ccccff">
 *     <th align=left>Parameter
 *     <th align=left>Represents
 *   <tr>
 *     <td>{d}
 *     <td>Date in default date format {@value #DEFAULT_DATE_FORMAT}
 *   <tr>
 *     <td>{d format}
 *     <td>Date in specific date format
 *   <tr>
 *     <td>{l}
 *     <td>Short name of log level. e.g: V/D/I
 *   <tr>
 *     <td>{L}
 *     <td>Long name of log level. e.g: VERBOSE/DEBUG/INFO
 *   <tr>
 *     <td>{t}
 *     <td>Tag of log
 *   <tr>
 *     <td>{m}
 *     <td>Message of log
 * </table>
 * </blockquote>
 * <p>
 * Imagine there is a log, with {@link LogLevel#DEBUG} level, "my_tag" tag and "Simple message"
 * message, the flattened log would be as below.
 * <blockquote>
 * <table border=0 cellspacing=3 cellpadding=0 summary="Examples of patterns and flattened logs">
 *   <tr bgcolor="#ccccff">
 *     <th align=left>Pattern
 *     <th align=left>Flattened log
 *   <tr>
 *     <td>{d} {l}/{t}: {m}
 *     <td>2016-11-30 13:00:00.000 D/my_tag: Simple message
 *   <tr>
 *     <td>{d yyyy-MM-dd HH:mm:ss.SSS} {l}/{t}: {m}
 *     <td>2016-11-30 13:00:00.000 D/my_tag: Simple message
 *   <tr>
 *     <td>{d yyyy/MM/dd HH:mm:ss} {l}|{t}: {m}
 *     <td>2016/11/30 13:00:00 D|my_tag: Simple message
 *   <tr>
 *     <td>{d yy/MM/dd HH:mm:ss} {l}|{t}: {m}
 *     <td>16/11/30 13:00:00 D|my_tag: Simple message
 *   <tr>
 *     <td>{d MM/dd HH:mm} {l}-{t}-{m}
 *     <td>11/30 13:00 D-my_tag-Simple message
 * </table>
 * </blockquote>
 *
 * @since 1.3.0
 */
public class PatternFlattener implements Flattener, Flattener2 {

  private static final String PARAM = "[^{}]*";
  private static final Pattern PARAM_REGEX = Pattern.compile("\\{(" + PARAM + ")\\}");

  private static final String PARAMETER_DATE = "d";
  private static final String PARAMETER_LEVEL_SHORT = "l";
  private static final String PARAMETER_LEVEL_LONG = "L";
  private static final String PARAMETER_TAG = "t";
  private static final String PARAMETER_MESSAGE = "m";

  static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

  private String pattern;

  private List<ParameterFiller> parameterFillers;

  /**
   * Constructor.
   *
   * @param pattern the format pattern to flatten the log
   */
  public PatternFlattener(String pattern) {
    if (pattern == null) {
      throw new NullPointerException("Pattern should not be null");
    }
    this.pattern = pattern;

    List<String> parameters = parsePattern(pattern);
    parameterFillers = parseParameters(parameters);
    if (parameterFillers.size() == 0) {
      throw new IllegalArgumentException("No recognizable parameter found in the pattern "
          + pattern);
    }
  }

  /**
   * Get the list of parameters from the given pattern.
   *
   * @param pattern the given pattern
   * @return the parameters list, or empty if no parameter found from the given pattern
   */
  static List<String> parsePattern(String pattern) {
    List<String> parameters = new ArrayList<>(4);
    Matcher matcher = PARAM_REGEX.matcher(pattern);
    while (matcher.find()) {
      parameters.add(matcher.group(1));
    }
    return parameters;
  }

  /**
   * Get the list of parameter fillers from the given parameters.
   *
   * @param parameters the given parameters
   * @return the parameter fillers, or empty if none of the parameters is recognizable
   */
  private static List<ParameterFiller> parseParameters(List<String> parameters) {
    List<ParameterFiller> parameterFillers = new ArrayList<>(parameters.size());
    for (String parameter : parameters) {
      ParameterFiller parameterFiller = parseParameter(parameter);
      if (parameterFiller != null) {
        parameterFillers.add(parameterFiller);
      }
    }
    return parameterFillers;
  }

  /**
   * Create a parameter filler if the given parameter is recognizable.
   *
   * @param parameter the given parameter
   * @return the created parameter filler, or null if can not recognize the given parameter
   */
  private static ParameterFiller parseParameter(String parameter) {
    String wrappedParameter = "{" + parameter + "}";
    String trimmedParameter = parameter.trim();
    ParameterFiller parameterFiller = parseDateParameter(wrappedParameter, trimmedParameter);
    if (parameterFiller != null) {
      return parameterFiller;
    }

    parameterFiller = parseLevelParameter(wrappedParameter, trimmedParameter);
    if (parameterFiller != null) {
      return parameterFiller;
    }

    parameterFiller = parseTagParameter(wrappedParameter, trimmedParameter);
    if (parameterFiller != null) {
      return parameterFiller;
    }

    parameterFiller = parseMessageParameter(wrappedParameter, trimmedParameter);
    if (parameterFiller != null) {
      return parameterFiller;
    }

    return null;
  }

  /**
   * Try to create a date filler if the given parameter is a date parameter.
   *
   * @return created date filler, or null if the given parameter is not a date parameter
   */
  static DateFiller parseDateParameter(String wrappedParameter, String trimmedParameter) {
    if (trimmedParameter.startsWith(PARAMETER_DATE + " ")
        && trimmedParameter.length() > PARAMETER_DATE.length() + 1) {
      String dateFormat = trimmedParameter.substring(PARAMETER_DATE.length() + 1);
      return new DateFiller(wrappedParameter, trimmedParameter, dateFormat);
    } else if (trimmedParameter.equals(PARAMETER_DATE)) {
      return new DateFiller(wrappedParameter, trimmedParameter, DEFAULT_DATE_FORMAT);
    }
    return null;
  }

  /**
   * Try to create a level filler if the given parameter is a level parameter.
   *
   * @return created level filler, or null if the given parameter is not a level parameter
   */
  static LevelFiller parseLevelParameter(String wrappedParameter, String trimmedParameter) {
    if (trimmedParameter.equals(PARAMETER_LEVEL_SHORT)) {
      return new LevelFiller(wrappedParameter, trimmedParameter, false);
    } else if (trimmedParameter.equals(PARAMETER_LEVEL_LONG)) {
      return new LevelFiller(wrappedParameter, trimmedParameter, true);
    }
    return null;
  }

  /**
   * Try to create a tag filler if the given parameter is a tag parameter.
   *
   * @return created tag filler, or null if the given parameter is not a tag parameter
   */
  static TagFiller parseTagParameter(String wrappedParameter, String trimmedParameter) {
    if (trimmedParameter.equals(PARAMETER_TAG)) {
      return new TagFiller(wrappedParameter, trimmedParameter);
    }
    return null;
  }

  /**
   * Try to create a message filler if the given parameter is a message parameter.
   *
   * @return created message filler, or null if the given parameter is not a message parameter
   */
  static MessageFiller parseMessageParameter(String wrappedParameter, String trimmedParameter) {
    if (trimmedParameter.equals(PARAMETER_MESSAGE)) {
      return new MessageFiller(wrappedParameter, trimmedParameter);
    }
    return null;
  }

  @Override
  public CharSequence flatten(int logLevel, String tag, String message) {
    return flatten(System.currentTimeMillis(), logLevel, tag, message);
  }

  @Override
  public CharSequence flatten(long timeMillis, int logLevel, String tag, String message) {
    String flattenedLog = pattern;
    for (ParameterFiller parameterFiller : parameterFillers) {
      flattenedLog = parameterFiller.fill(flattenedLog, timeMillis, logLevel, tag, message);
    }
    return flattenedLog;
  }

  /**
   * Fill the original pattern string with formatted date string.
   */
  static class DateFiller extends ParameterFiller {

    String dateFormat;

    private ThreadLocal<SimpleDateFormat> threadLocalDateFormat = new ThreadLocal<SimpleDateFormat>() {

      @Override
      protected SimpleDateFormat initialValue() {
        return new SimpleDateFormat(dateFormat, Locale.US);
      }
    };

    DateFiller(String wrappedParameter, String trimmedParameter, String dateFormat) {
      super(wrappedParameter, trimmedParameter);
      this.dateFormat = dateFormat;

      try {
        // Test the format, will throw an exception if it is a bad format.
        threadLocalDateFormat.get().format(new Date());
      } catch (Exception e) {
        throw new IllegalArgumentException("Bad date pattern: " + dateFormat, e);
      }
    }

    @Override
    protected String fill(String pattern, long timeMillis, int logLevel, String tag, String message) {
      return pattern.replace(wrappedParameter, threadLocalDateFormat.get().format(new Date(timeMillis)));
    }
  }

  /**
   * Fill the original pattern string with level name.
   */
  static class LevelFiller extends ParameterFiller {

    boolean useLongName;

    LevelFiller(String wrappedParameter, String trimmedParameter, boolean useLongName) {
      super(wrappedParameter, trimmedParameter);
      this.useLongName = useLongName;
    }

    @Override
    protected String fill(String pattern, long timeMillis, int logLevel, String tag, String message) {
      if (useLongName) {
        return pattern.replace(wrappedParameter, LogLevel.getLevelName(logLevel));
      } else {
        return pattern.replace(wrappedParameter, LogLevel.getShortLevelName(logLevel));
      }
    }
  }

  /**
   * Fill the original pattern string with tag.
   */
  static class TagFiller extends ParameterFiller {

    TagFiller(String wrappedParameter, String trimmedParameter) {
      super(wrappedParameter, trimmedParameter);
    }

    @Override
    protected String fill(String pattern, long timeMillis, int logLevel, String tag, String message) {
      return pattern.replace(wrappedParameter, tag);
    }
  }

  /**
   * Fill the original pattern string with message.
   */
  static class MessageFiller extends ParameterFiller {

    MessageFiller(String wrappedParameter, String trimmedParameter) {
      super(wrappedParameter, trimmedParameter);
    }

    @Override
    protected String fill(String pattern, long timeMillis, int logLevel, String tag, String message) {
      return pattern.replace(wrappedParameter, message);
    }
  }

  /**
   * Fill the original pattern string with the value of parameter.
   */
  abstract static class ParameterFiller {

    /**
     * The parameter parsed from the original pattern string, in a format of "{parameter}", maybe
     * spaces around the parameter and within the "{" and "}".
     */
    String wrappedParameter;

    /**
     * The trimmed parameter, and without "{" and "}" around it.
     */
    String trimmedParameter;

    ParameterFiller(String wrappedParameter, String trimmedParameter) {
      this.wrappedParameter = wrappedParameter;
      this.trimmedParameter = trimmedParameter;
    }

    /**
     * Fill the original pattern string with the value of parameter.
     *
     * @param pattern    the original pattern
     * @param timeMillis the time milliseconds of log
     * @param logLevel   the log level of flattening log
     * @param tag        the tag of flattening log
     * @param message    the message of the flattening log
     * @return the filled pattern string
     */
    protected abstract String fill(String pattern, long timeMillis, int logLevel, String tag, String message);
  }
}
