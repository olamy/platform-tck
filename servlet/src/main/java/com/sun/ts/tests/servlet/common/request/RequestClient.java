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

package com.sun.ts.tests.servlet.common.request;

import com.sun.ts.tests.servlet.common.client.AbstractUrlClient;
import com.sun.ts.tests.servlet.common.util.Data;
import org.junit.jupiter.api.Test;

public class RequestClient extends AbstractUrlClient {

  @Test
  public void getAttributeNamesTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getAttributeNamesTest");
    invoke();
  }

  @Test
  public void getAttributeNamesEmptyEnumTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getAttributeNamesEmptyEnumTest");
    invoke();
  }

  @Test
  public void getAttributeTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getAttributeTest");
    invoke();
  }

  @Test
  public void getAttributeDoesNotExistTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getAttributeDoesNotExistTest");
    invoke();
  }

  @Test
  public void getCharacterEncodingTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getCharacterEncodingTest");
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "Content-Type:text/plain; charset=ISO-8859-1");
    invoke();
  }

  @Test
  public void getContentLengthTest() throws Exception {
    String testName = "getContentLengthTest";
    TEST_PROPS.setProperty(REQUEST, "POST " + getContextRoot() + "/"
        + getServletName() + "?testname=" + testName + " HTTP/1.1");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Content-Type:text/plain");
    TEST_PROPS.setProperty(CONTENT, "calling getContentLengthTest");
    TEST_PROPS.setProperty(SEARCH_STRING, Data.PASSED);
    invoke();
  }

  @Test
  public void getContentTypeTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getContentTypeTest");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Content-Type:text/plain");
    invoke();
  }

  @Test
  public void getContentTypeNullTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getContentTypeNullTest");
    invoke();
  }

  public void getInputStreamTest() throws Exception {
    String testName = "getInputStreamTest";
    TEST_PROPS.setProperty(REQUEST, "POST " + getContextRoot() + "/"
        + getServletName() + "?testname=" + testName + " HTTP/1.1");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Content-Type:text/plain");
    TEST_PROPS.setProperty(CONTENT, "calling getInputStreamTest");
    TEST_PROPS.setProperty(SEARCH_STRING, Data.PASSED);
    invoke();
  }

  @Test
  public void getInputStreamIllegalStateExceptionTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getInputStreamIllegalStateExceptionTest");
    invoke();
  }

  @Test
  public void getParameterValuesDoesNotExistTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getParameterValuesDoesNotExistTest");
    invoke();
  }

  @Test
  public void getLocaleTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getLocaleTest");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language:en-US");
    invoke();
  }

  @Test
  public void getLocaleDefaultTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getLocaleDefaultTest");
    invoke();
  }

  @Test
  public void getLocalesTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getLocalesTest");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language:en-US,en-GB");
    invoke();
  }

  @Test
  public void getLocalesDefaultTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getLocalesDefaultTest");
    invoke();
  }

  @Test
  public void getParameterMapTest() throws Exception {
    String testName = "getParameterMapTest";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + getServletName() + "?testname="
            + testName + "&parameter1=value1&parameter2=value2 HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, Data.PASSED);
    invoke();
  }

  @Test
  public void getParameterNamesTest() throws Exception {
    String testName = "getParameterNamesTest";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + getServletName() + "?testname="
            + testName + "&parameter1=value1&parameter2=value2 HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, Data.PASSED);
    invoke();
  }

  @Test
  public void getParameterNamesEmptyEnumTest() throws Exception {
    String testName = "getParameterNamesEmptyEnumTest";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + testName + " HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, Data.PASSED);
    invoke();
  }

  @Test
  public void getParameterTest() throws Exception {
    String testName = "getParameterTest";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + getServletName() + "?testname="
            + testName + "&parameter1=value1 HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, Data.PASSED);
    invoke();
  }

  @Test
  public void getParameterDoesNotExistTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getParameterDoesNotExistTest");
    invoke();
  }

  @Test
  public void getParameterValuesTest() throws Exception {
    String testName = "getParameterValuesTest";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + getServletName() + "?testname="
            + testName + "&Names=value1&Names=value2 HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, Data.PASSED);
    invoke();
  }

  @Test
  public void getProtocolTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getProtocolTest");
    invoke();
  }

  @Test
  public void getReaderTest() throws Exception {
    String testName = "getReaderTest";
    TEST_PROPS.setProperty(REQUEST, "POST " + getContextRoot() + "/"
        + getServletName() + "?testname=" + testName + " HTTP/1.1");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Content-Type:text/plain");
    TEST_PROPS.setProperty(CONTENT, "calling getReaderTest");
    TEST_PROPS.setProperty(SEARCH_STRING, Data.PASSED);
    invoke();
  }

  @Test
  public void getReaderIllegalStateExceptionTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getReaderIllegalStateExceptionTest");
    invoke();
  }

  @Test
  public void getReaderUnsupportedEncodingExceptionTest() throws Exception {
    String testName = "getReaderUnsupportedEncodingExceptionTest";
    TEST_PROPS.setProperty(REQUEST,
        "POST " + getContextRoot() + "/" + testName + " HTTP/1.1");
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "Content-Type:text/plain; charset=DoesNonExist");
    TEST_PROPS.setProperty(CONTENT,
        "calling getReaderUnsupportedEncodingExceptionTest");
    TEST_PROPS.setProperty(SEARCH_STRING, Data.PASSED);
    invoke();
  }

  @Test
  public void getRemoteAddrTest() throws Exception {
    String localIps = getLocalInterfaceInfo(true);
    String testName = "getRemoteAddrTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + getServletName() + "?testname="
            + testName + "&Address=" + localIps + " HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, Data.PASSED);
    invoke();
  }

  @Test
  public void getLocalAddrTest() throws Exception {
    String testName = "getLocalAddrTest";
    TEST_PROPS.setProperty(APITEST, testName);
    TEST_PROPS.setProperty(SEARCH_STRING, Data.PASSED);
    invoke();
  }

  @Test
  public void getRemoteHostTest() throws Exception {
    String testName = "getRemoteHostTest";
    String localIps = getLocalInterfaceInfo(true);
    String localHosts = getLocalInterfaceInfo(false);
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + getServletName() + "?testname="
            + testName + "&Address=" + localIps + "," + localHosts
            + " HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, Data.PASSED);
    invoke();
  }

  @Test
  public void getRequestDispatcherTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getRequestDispatcherTest");
    invoke();
  }

  @Test
  public void getSchemeTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getSchemeTest");
    invoke();
  }

  @Test
  public void getServerNameTest() throws Exception {
    String testName = "getServerNameTest";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + getServletName() + "?testname="
            + testName + "&hostname=" + _hostname + " HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, Data.PASSED);
    invoke();
  }

  @Test
  public void getServerPortTest() throws Exception {
    String testName = "getServerPortTest";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + getServletName() + "?testname="
            + testName + "&port=" + _port + " HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, Data.PASSED);
    invoke();
  }

  @Test
  public void isSecureTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "isSecureTest");
    invoke();
  }

  @Test
  public void removeAttributeTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "removeAttributeTest");
    invoke();
  }

  @Test
  public void setAttributeTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "setAttributeTest");
    invoke();
  }

  @Test
  public void getCharacterEncodingNullTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getCharacterEncodingNullTest");
    invoke();
  }

  @Test
  public void setCharacterEncodingUnsupportedEncodingExceptionTest()
      throws Exception {
    String testName = "setCharacterEncodingUnsupportedEncodingExceptionTest";
    TEST_PROPS.setProperty(REQUEST,
        "POST " + getContextRoot() + "/" + testName + " HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, Data.PASSED);
    invoke();
  }

  @Test
  public void setCharacterEncodingTest() throws Exception {
    String testName = "setCharacterEncodingTest";
    TEST_PROPS.setProperty(REQUEST,
        "POST " + getContextRoot() + "/" + testName + " HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, Data.PASSED);
    invoke();
  }

  @Test
  public void setCharacterEncodingTest1() throws Exception {
    String testName = "setCharacterEncodingTest1";
    TEST_PROPS.setProperty(APITEST, testName);
    invoke();
  }

  @Test
  public void getLocalNameTest() throws Exception {
    String testName = "getLocalNameTest";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + getServletName() + "?testname="
            + testName + "&hostname=" + _hostname + " HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, Data.PASSED);

    invoke();
  }

  @Test
  public void getLocalPortTest() throws Exception {
    String testName = "getLocalPortTest";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + getServletName() + "?testname="
            + testName + "&hostport=" + _port + " HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, Data.PASSED);
    invoke();
  }
}
