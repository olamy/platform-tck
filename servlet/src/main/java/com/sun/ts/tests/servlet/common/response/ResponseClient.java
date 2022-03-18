/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

/*
 * $Id$
 */

package com.sun.ts.tests.servlet.common.response;

import com.sun.ts.tests.servlet.common.client.AbstractUrlClient;
import com.sun.ts.tests.servlet.common.util.Data;
import org.junit.jupiter.api.Test;

public class ResponseClient extends AbstractUrlClient {

  @Test
  public void flushBufferTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "flushBufferTest");
    invoke();
  }

  @Test
  public void getBufferSizeTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getBufferSizeTest");
    invoke();
  }

  @Test
  public void getLocaleDefaultTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getLocaleDefaultTest");
    invoke();
  }

  @Test
  public void getLocaleTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getLocaleTest");
    invoke();
  }

  @Test
  public void getOutputStreamTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getOutputStreamTest");
    invoke();
  }

  @Test
  public void getOutputStreamFlushTest() throws Exception {
    String testName = "getOutputStreamFlushTest";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST, "GET " + getContextRoot() + "/"
        + getServletName() + "?testname=" + testName + " HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "in getOutputStreamFlushTest|" + Data.PASSED);
    invoke();
  }

  @Test
  public void getOutputStreamIllegalStateExceptionTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getOutputStreamIllegalStateExceptionTest");
    invoke();
  }

  @Test
  public void getWriterTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getWriterTest");
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "Content-Type: text/html;charset=ISO-8859-1");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "Content-Type: text/html");
    TEST_PROPS.setProperty(SEARCH_STRING, Data.PASSED);
    invoke();
  }

  @Test
  public void getWriterFlushTest() throws Exception {
    String testName = "getWriterFlushTest";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST, "GET " + getContextRoot() + "/"
        + getServletName() + "?testname=" + testName + " HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "in test|" + Data.PASSED);
    invoke();
  }

  @Test
  public void getWriterAfterTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getWriterAfterTest");
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "Content-Type: text/html;charset=ISO-8859-1");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Content-Type: text/html;charset=ISO-8859-7");
    invoke();
  }

  @Test
  public void getWriterIllegalStateExceptionTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getWriterIllegalStateExceptionTest");
    invoke();
  }

  @Test
  public void getWriterUnsupportedEncodingExceptionTest() throws Exception {
    String testName = "getWriterUnsupportedEncodingExceptionTest";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + testName + " HTTP/1.1");
    invoke();
    testName = "checkTestResult";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + testName + " HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, Data.PASSED);
    invoke();
  }

  @Test
  public void isCommittedTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "isCommittedTest");
    invoke();
  }

  @Test
  public void resetBufferTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "resetBufferTest");
    invoke();
  }

  @Test
  public void resetTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "resetTest");
    invoke();
  }

  @Test
  public void resetTest1() throws Exception {
    TEST_PROPS.setProperty(APITEST, "resetTest1");
    TEST_PROPS.setProperty(UNEXPECTED_HEADERS,
        "Content-Type: application/java-archive; charset=Shift_Jis");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "BigNoNo");
    TEST_PROPS.setProperty(SEARCH_STRING, "YesPlease");
    invoke();
  }

  @Test
  public void resetIllegalStateExceptionTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "resetIllegalStateExceptionTest");
    invoke();
  }

  @Test
  public void getCharacterEncodingTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getCharacterEncodingTest");
    invoke();
  }

  @Test
  public void getCharacterEncodingDefaultTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getCharacterEncodingDefaultTest");
    invoke();
  }

  @Test
  public void setCharacterEncodingTest() throws Exception {
    String testName = "setCharacterEncodingTest";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "Content-Type: text/html;charset=ISO-8859-1");
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + testName + " HTTP/1.1");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Content-Type: text/html;charset=ISO-8859-7");
    TEST_PROPS.setProperty(SEARCH_STRING, Data.PASSED);
    invoke();
  }

  @Test
  public void setBufferSizeTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "setBufferSizeTest");
    invoke();
  }

  @Test
  public void setBufferSizeIllegalStateExceptionTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "setBufferSizeIllegalStateExceptionTest");
    invoke();
  }

  @Test
  public void setContentLengthTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "setContentLengthTest");
    int lenn = Data.PASSED.length();
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "Content-Length:" + lenn);
    invoke();
  }

  @Test
  public void getContentTypeTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getContentTypeTest");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Content-Type:text/html");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Content-Type:text/html;charset=ISO-8859-1");
    invoke();
  }

  @Test
  public void getContentType1Test() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getContentType1Test");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "Content-Type:text/html");
    invoke();
  }

  @Test
  public void getContentTypeNullTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getContentTypeNullTest");
    invoke();
  }

  @Test
  public void getContentTypeNull1Test() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getContentTypeNull1Test");
    invoke();
  }

  @Test
  public void getContentTypeNull2Test() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getContentTypeNull2Test");
    invoke();
  }

  @Test
  public void setContentTypeTest() throws Exception {
    String testName = "setContentTypeTest";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST, "GET " + getContextRoot() + "/"
        + getServletName() + "?testname=" + testName + " HTTP/1.1");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "Content-Type:text/html");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    invoke();
  }

  @Test
  public void setContentType1Test() throws Exception {
    TEST_PROPS.setProperty(APITEST, "setContentType1Test");
    invoke();
  }

  @Test
  public void setContentType2Test() throws Exception {
    String testName = "setContentType2Test";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST, "GET " + getContextRoot() + "/"
        + getServletName() + "?testname=" + testName + " HTTP/1.1");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "Content-Type:text/html");
    TEST_PROPS.setProperty(UNEXPECTED_HEADERS, "Content-Type:text/plain");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    invoke();
  }

  @Test
  public void setLocaleTest() throws Exception {
    String testName = "setLocaleTest";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST, "GET " + getContextRoot() + "/"
        + getServletName() + "?testname=" + testName + " HTTP/1.1");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "Content-Language:en-US");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    invoke();
  }

  @Test
  public void setLocale1Test() throws Exception {
    String testName = "setLocale1Test";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST, "GET " + getContextRoot() + "/"
        + getServletName() + "?testname=" + testName + " HTTP/1.1");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Content-Type:text/html;charset=Shift_Jis");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    invoke();
  }
}
