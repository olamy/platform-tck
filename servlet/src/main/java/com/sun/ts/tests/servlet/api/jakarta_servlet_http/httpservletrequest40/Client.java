/*
 * Copyright (c) 2017, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * $Id:$
 */
package com.sun.ts.tests.servlet.api.jakarta_servlet_http.httpservletrequest40;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Properties;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.WebUtil;
import com.sun.ts.tests.servlet.common.client.AbstractUrlClient;

public class Client extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/servlet_jsh_httpservletrequest40_web";

  private static final String PROTOCOL = "http";

  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  public static final String DELIMITER = "\r\n";

  public static final String ENCODING = "ISO-8859-1";

  private String hostname;

  private int portnum;

  private WebUtil.Response response = null;

  private String request = null;

  /*
   * @class.setup_props: webServerHost; webServerPort;
   */
  public void setup(String[] args, Properties p) throws Exception {
    boolean pass = true;

    try {
      hostname = p.getProperty(WEBSERVERHOSTPROP);
      if (hostname == null)
        pass = false;
      else if (hostname.equals(""))
        pass = false;
      try {
        portnum = Integer.parseInt(p.getProperty(WEBSERVERPORTPROP));
      } catch (Exception e) {
        pass = false;
      }
    } catch (Exception e) {
      throw new Exception("setup failed:", e);
    }
    if (!pass) {
      TestUtil.logErr(
          "Please specify host & port of web server " + "in config properties: "
              + WEBSERVERHOSTPROP + ", " + WEBSERVERPORTPROP);
      throw new Exception("setup failed:");
    }

    System.out.println(hostname);
    System.out.println(portnum);
    logMsg("setup ok");
  }

  public void cleanup() throws Exception {
    TestUtil.logTrace("cleanup");
  }

  /*
   * @testName: httpServletMappingTest
   * 
   * @assertion_ids: servlet40:httpServletMappingTest;
   * 
   * @test_Strategy:
   */
  public void httpServletMappingTest() throws Exception {
    simpleTest("httpServletMappingTest", CONTEXT_ROOT + "/TestServlet", "GET",
        "matchValue=TestServlet, pattern=/TestServlet, servletName=TestServlet, mappingMatch=EXACT");
  }

  /*
   * @testName: httpServletMappingTest2
   * 
   * @assertion_ids: servlet40:httpServletMappingTest2;
   * 
   * @test_Strategy:
   */
  public void httpServletMappingTest2() throws Exception {
    simpleTest("httpServletMappingTest2", CONTEXT_ROOT + "/a.ts", "GET",
        "matchValue=a, pattern=*.ts, servletName=TestServlet, mappingMatch=EXTENSION");
  }

  /*
   * @testName: httpServletMappingTest3
   * 
   * @assertion_ids: servlet40:httpServletMappingTest3;
   * 
   * @test_Strategy:
   */
  public void httpServletMappingTest3() throws Exception {
    simpleTest("httpServletMappingTest3", CONTEXT_ROOT + "/default", "GET", 
        "matchValue=, pattern=/, servletName=defaultServlet, mappingMatch=DEFAULT");
  }

  /*
   * @testName: httpServletMappingForwardTest
   * 
   * @assertion_ids: servlet40:httpServletMappingForwardTest;
   * 
   * @test_Strategy:
   */
  public void httpServletMappingForwardTest() throws Exception {
    simpleTest("httpServletMappingForwardTest",
        CONTEXT_ROOT + "/ForwardServlet", "GET",
        "matchValue=a, pattern=*.ts, servletName=TestServlet, mappingMatch=EXTENSION");
  }

  /*
   * @testName: httpServletMappingNamedForwardTest
   * 
   * @assertion_ids: servlet40:httpServletMappingNamedForwardTest;
   * 
   * @test_Strategy:
   */
  public void httpServletMappingNamedForwardTest() throws Exception {
    simpleTest("httpServletMappingNamedForwardTest",
        CONTEXT_ROOT + "/NamedForwardServlet", "GET",
        "matchValue=NamedForwardServlet, pattern=/NamedForwardServlet, servletName=NamedForwardServlet, mappingMatch=EXACT");
  }

  /*
   * @testName: httpServletMappingNamedIncludeTest
   * 
   * @assertion_ids: servlet40:httpServletMappingNamedIncludeTest;
   * 
   * @test_Strategy:
   */
  public void httpServletMappingNamedIncludeTest() throws Exception {
    simpleTest("httpServletMappingNamedIncludeTest",
        CONTEXT_ROOT + "/NamedIncludeServlet", "GET",
        "matchValue=NamedIncludeServlet, pattern=/NamedIncludeServlet, servletName=NamedIncludeServlet, mappingMatch=EXACT");
  }

  /*
   * @testName: httpServletMappingIncludeTest
   * 
   * @assertion_ids: servlet40:httpServletMappingIncludeTest;
   * 
   * @test_Strategy:
   */
  public void httpServletMappingIncludeTest() throws Exception {
    simpleTest("httpServletMappingIncludeTest",
        CONTEXT_ROOT + "/IncludeServlet", "POST",
        "matchValue=IncludeServlet, pattern=/IncludeServlet, servletName=IncludeServlet, mappingMatch=EXACT");
  }

  /*
   * @testName: httpServletMappingFilterTest
   * 
   * @assertion_ids: servlet40:httpServletMappingFilterTest;
   * 
   * @test_Strategy:
   */
  public void httpServletMappingFilterTest() throws Exception {
    simpleTest("httpServletMappingFilterTest", CONTEXT_ROOT + "/ForwardFilter",
        "GET",
        "matchValue=, pattern=/, servletName=defaultServlet, mappingMatch=DEFAULT");
  }

  /*
   * @testName: httpServletMappingDispatchTest
   * 
   * @assertion_ids: servlet40:httpServletMappingDispatchTest;
   * 
   * @test_Strategy:
   */
  public void httpServletMappingDispatchTest() throws Exception {
    simpleTest("httpServletMappingDispatchTest",
        CONTEXT_ROOT + "/DispatchServlet", "GET",
        "matchValue=DispatchServlet, pattern=/DispatchServlet, servletName=DispatchServlet, mappingMatch=EXACT");
  }

  private void simpleTest(String testName, String request, String method,
      String expected) throws Exception {
    try {
      TestUtil.logMsg("Sending request \"" + request + "\"");

      response = WebUtil.sendRequest(method, InetAddress.getByName(hostname),
          portnum, getRequest(request), null, null);

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception(testName + " failed: ", e);
    }

    TestUtil.logMsg("response.statusToken:" + response.statusToken);
    TestUtil.logMsg("response.content:" + response.content);

    // Check that the page was found (no error).
    if (response.isError()) {
      TestUtil.logErr("Could not find " + request);
      throw new Exception(testName + " failed.");
    }

    if (response.content.indexOf(expected) < 0) {
      TestUtil.logMsg("Expected: " + expected);
      throw new Exception(testName + " failed.");
    }
  }

  /*
   * @testName: TrailerTest
   * 
   * @assertion_ids: servlet40:TrailerTest;
   * 
   * @test_Strategy:
   */
  public void TrailerTest() throws Exception {
    URL url;
    Socket socket = null;
    OutputStream output;
    InputStream input;

    try {
      url = new URL("http://" + hostname + ":" + portnum + CONTEXT_ROOT
          + "/TrailerTestServlet");
      socket = new Socket(url.getHost(), url.getPort());
      socket.setKeepAlive(true);
      output = socket.getOutputStream();

      String path = url.getPath();
      StringBuffer outputBuffer = new StringBuffer();
      outputBuffer.append("POST " + path + " HTTP/1.1" + DELIMITER);
      outputBuffer.append("Host: " + url.getHost() + DELIMITER);
      outputBuffer.append("Connection: keep-alive" + DELIMITER);
      outputBuffer.append("Content-Type: text/plain" + DELIMITER);
      outputBuffer.append("Transfer-Encoding: chunked" + DELIMITER);
      outputBuffer.append("Trailer: myTrailer, myTrailer2" + DELIMITER);
      outputBuffer.append(DELIMITER);
      outputBuffer.append("3" + DELIMITER);
      outputBuffer.append("ABC" + DELIMITER);
      outputBuffer.append("0" + DELIMITER);
      outputBuffer.append("myTrailer:foo");
      outputBuffer.append(DELIMITER);
      outputBuffer.append("myTrailer2:bar");
      outputBuffer.append(DELIMITER);
      outputBuffer.append(DELIMITER);

      byte[] outputBytes = outputBuffer.toString().getBytes(ENCODING);
      output.write(outputBytes);
      output.flush();

      input = socket.getInputStream();
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      int read = 0;
      while ((read = input.read()) >= 0) {
        bytes.write(read);
      }
      String response = new String(bytes.toByteArray());
      TestUtil.logMsg(response);
      if (response.indexOf("isTrailerFieldsReady: true") < 0) {
        TestUtil.logErr("isTrailerFieldsReady should be true");
        throw new Exception("TrailerTest failed.");
      }

      if (response.toLowerCase().indexOf("mytrailer=foo") < 0) {
        TestUtil.logErr("failed to get trailer field: mytrailer=foo");
        throw new Exception("TrailerTest failed.");
      }

      if (response.toLowerCase().indexOf("mytrailer2=bar") < 0) {
        TestUtil.logErr("failed to get trailer field: mytrailer=foo");
        throw new Exception("TrailerTest failed.");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("TrailerTest failed: ", e);
    } finally {
      try {
        if (socket != null)
          socket.close();
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: TrailerTest2
   * 
   * @assertion_ids: servlet40:TrailerTest2;
   * 
   * @test_Strategy:
   */
  public void TrailerTest2() throws Exception {
    simpleTest("TrailerTest2", CONTEXT_ROOT + "/TrailerTestServlet", "POST",
        "isTrailerFieldsReady: true");
    simpleTest("TrailerTest2", CONTEXT_ROOT + "/TrailerTestServlet", "POST",
        "Trailer: {}");
  }
}
