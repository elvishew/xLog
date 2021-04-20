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

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PatternFlattenerTest {

  @Test
  public void testParsePattern() {
    List<String> parameters = PatternFlattener.parsePattern(
        "{d yyyy-MM-dd hh:mm:ss.SSS} {l}/{t}: {m}");
    assertNotNull(parameters);
    assertEquals(4, parameters.size());
    assertEquals("d yyyy-MM-dd hh:mm:ss.SSS", parameters.get(0));
    assertEquals("l", parameters.get(1));
    assertEquals("t", parameters.get(2));
    assertEquals("m", parameters.get(3));

    parameters = PatternFlattener.parsePattern(
        "Abc { d yyyy  } {l }/{ t}: { m } def");
    assertNotNull(parameters);
    assertEquals(4, parameters.size());
    assertEquals(" d yyyy  ", parameters.get(0));
    assertEquals("l ", parameters.get(1));
    assertEquals(" t", parameters.get(2));
    assertEquals(" m ", parameters.get(3));

    parameters = PatternFlattener.parsePattern(
        "No valid parameter {f }");
    assertNotNull(parameters);
    assertEquals(1, parameters.size());
    assertEquals("f ", parameters.get(0));

    parameters = PatternFlattener.parsePattern(
        "No parameter");
    assertNotNull(parameters);
    assertEquals(0, parameters.size());
  }

  @Test
  public void testParseDateParameter() {
    // Test date format.
    PatternFlattener.ParameterFiller parameterFiller = PatternFlattener.parseDateParameter(
        "{d yyyy}", "d yyyy");
    assertNoNullAndClass(parameterFiller, PatternFlattener.DateFiller.class);
    assertEquals("yyyy", ((PatternFlattener.DateFiller) parameterFiller).dateFormat);

    // Test default date format.
    parameterFiller = PatternFlattener.parseDateParameter("{d}", "d");
    assertNoNullAndClass(parameterFiller, PatternFlattener.DateFiller.class);
    assertEquals(PatternFlattener.DEFAULT_DATE_FORMAT,
        ((PatternFlattener.DateFiller) parameterFiller).dateFormat);

    // Test invalid format.
    parameterFiller = PatternFlattener.parseDateParameter("{D}", "D");
    assertNull(parameterFiller);
  }

  @Test
  public void testParseLevelParameter() {
    // Test short level format.
    PatternFlattener.ParameterFiller parameterFiller = PatternFlattener.parseLevelParameter("{l}", "l");
    assertNoNullAndClass(parameterFiller, PatternFlattener.LevelFiller.class);
    assertFalse(((PatternFlattener.LevelFiller) parameterFiller).useLongName);

    // Test long level format.
    parameterFiller = PatternFlattener.parseLevelParameter("{L}", "L");
    assertNoNullAndClass(parameterFiller, PatternFlattener.LevelFiller.class);
    assertTrue(((PatternFlattener.LevelFiller) parameterFiller).useLongName);

    // Test invalid format.
    parameterFiller = PatternFlattener.parseDateParameter("{ll}", "ll");
    assertNull(parameterFiller);
  }

  @Test
  public void testParseTagParameter() {
    PatternFlattener.ParameterFiller parameterFiller = PatternFlattener.parseTagParameter(
        "{t}", "t");
    assertNoNullAndClass(parameterFiller, PatternFlattener.TagFiller.class);

    // Test invalid format.
    parameterFiller = PatternFlattener.parseDateParameter("{T}", "T");
    assertNull(parameterFiller);
  }

  @Test
  public void testParseMessageParameter() {
    PatternFlattener.ParameterFiller parameterFiller = PatternFlattener.parseMessageParameter(
        "{m}", "m");
    assertNoNullAndClass(parameterFiller, PatternFlattener.MessageFiller.class);

    // Test invalid format.
    parameterFiller = PatternFlattener.parseDateParameter("{M}", "M");
    assertNull(parameterFiller);
  }

  private void assertNoNullAndClass(PatternFlattener.ParameterFiller parameterFiller, Class<?> clazz) {
    assertNotNull("Parameter filler not created", parameterFiller);
    assertTrue("Parameter filler class not expected: " + parameterFiller.getClass(),
        parameterFiller.getClass() == clazz);
  }
}