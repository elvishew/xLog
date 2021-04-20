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

package com.elvishew.xlog.formatter.message.xml;

import com.elvishew.xlog.internal.SystemCompat;
import com.elvishew.xlog.formatter.FormatException;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Simply format the XML with a indent of {@value XML_INDENT}.
 * <br>TODO: Make indent size and enable/disable state configurable.
 */
public class DefaultXmlFormatter implements XmlFormatter {

  private static final int XML_INDENT = 4;

  @Override
  public String format(String xml) {
    String formattedString;
    if (xml == null || xml.trim().length() == 0) {
      throw new FormatException("XML empty.");
    }
    try {
      Source xmlInput = new StreamSource(new StringReader(xml));
      StreamResult xmlOutput = new StreamResult(new StringWriter());
      Transformer transformer = TransformerFactory.newInstance().newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",
          String.valueOf(XML_INDENT));
      transformer.transform(xmlInput, xmlOutput);
      formattedString = xmlOutput.getWriter().toString().replaceFirst(">", ">"
          + SystemCompat.lineSeparator);
    } catch (Exception e) {
      throw new FormatException("Parse XML error. XML string:" + xml, e);
    }
    return formattedString;
  }
}
