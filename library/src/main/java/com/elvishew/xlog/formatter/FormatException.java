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

package com.elvishew.xlog.formatter;

/**
 * Thrown to indicate that the format of the data is something wrong.
 */
public class FormatException extends RuntimeException {
  /**
   * Constructs a <code>FormatException</code> with no
   * detail message.
   */
  public FormatException() {
    super();
  }

  /**
   * Constructs a <code>FormatException</code> with the
   * specified detail message.
   *
   * @param s the detail message.
   */
  public FormatException(String s) {
    super(s);
  }

  /**
   * Constructs a new exception with the specified detail message and
   * cause.
   * <p>
   * <p>Note that the detail message associated with <code>cause</code> is
   * <i>not</i> automatically incorporated in this exception's detail
   * message.
   *
   * @param message the detail message (which is saved for later retrieval
   *                by the {@link Throwable#getMessage()} method).
   * @param cause   the cause (which is saved for later retrieval by the
   *                {@link Throwable#getCause()} method).  (A <tt>null</tt> value
   *                is permitted, and indicates that the cause is nonexistent or
   *                unknown.)
   */
  public FormatException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a new exception with the specified cause and a detail
   * message of <tt>(cause==null ? null : cause.toString())</tt> (which
   * typically contains the class and detail message of <tt>cause</tt>).
   * This constructor is useful for exceptions that are little more than
   * wrappers for other throwables (for example, {@link
   * java.security.PrivilegedActionException}).
   *
   * @param cause the cause (which is saved for later retrieval by the
   *              {@link Throwable#getCause()} method).  (A <tt>null</tt> value is
   *              permitted, and indicates that the cause is nonexistent or
   *              unknown.)
   */
  public FormatException(Throwable cause) {
    super(cause);
  }

  private static final long serialVersionUID = -5365630128856068164L;
}
