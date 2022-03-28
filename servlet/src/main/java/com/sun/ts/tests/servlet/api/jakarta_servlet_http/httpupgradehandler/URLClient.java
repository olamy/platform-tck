/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates and others.
 * All rights reserved.
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

package com.sun.ts.tests.servlet.api.jakarta_servlet_http.httpupgradehandler;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.servlet.common.client.AbstractUrlClient;
import com.sun.ts.tests.servlet.common.util.ServletTestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;

public class URLClient extends AbstractUrlClient {

  private static final String CRLF = "\r\n";

  @BeforeEach
  public void setupServletName() throws Exception {
    setServletName("TestServlet");
  }

  /**
   * Deployment for the test
   */
  @Deployment(testable = false)
  public static WebArchive getTestArchive() throws Exception {
    return ShrinkWrap.create(WebArchive.class, "servlet_jsh_upgradehandler_web.war")
            .setWebXML(URLClient.class.getResource("servlet_jsh_upgradehandler_web.xml"));
  }


  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   * servlet_async_wait;
   */
  /* Run test */
  /*
   * @testName: upgradeTest
   *
   * @assertion_ids: Servlet:JAVADOC:905; Servlet:JAVADOC:909;
   * Servlet:JAVADOC:911; Servlet:JAVADOC:923; Servlet:JAVADOC:925;
   * Servlet:JAVADOC:930; Servlet:JAVADOC:937;
   *
   * @test_Strategy: Create a Servlet TestServlet; From Client, sends upgrade
   * request with two batch of messages to the Servlet; Servlet upgrade the
   * request accordingly; Create a ReadListener; Verify all message received;
   * Verify UpgradeHandler accordingly Verify ReadListener works accordingly
   */
  @Test
  public void upgradeTest() throws Exception {
    Boolean passed1 = false;
    Boolean passed2 = false;
    Boolean passed3 = false;
    String EXPECTED_RESPONSE1 = "TCKHttpUpgradeHandler.init";
    String EXPECTED_RESPONSE2 = "onDataAvailable|Hello";
    String EXPECTED_RESPONSE3 = "onDataAvailable|World";

    InputStream input = null;
    OutputStream output = null;
    Socket s = null;

    String requestUrl = getContextRoot() + "/" + getServletName() + " HTTP/1.1";

    try {
      URL url = getURL("http", _hostname, _port, requestUrl);

      s = new Socket(_hostname, _port);
      output = s.getOutputStream();

      StringBuffer reqStr = new StringBuffer("POST "
          + url.toExternalForm().replace("http://", "").replace(_hostname, "")
              .replace(":" + Integer.toString(_port), "")
          + CRLF);
      reqStr.append("User-Agent: Java/1.6.0_33" + CRLF);
      reqStr.append("Host: " + _hostname + ":" + _port + CRLF);
      reqStr
          .append("Accept: text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2"
              + CRLF);
      reqStr.append("Upgrade: YES" + CRLF);
      reqStr.append("Connection: Upgrade" + CRLF);
      reqStr.append("Content-type: application/x-www-form-urlencoded" + CRLF);
      reqStr.append(CRLF);

      TestUtil.logMsg("REQUEST=========" + reqStr.toString());
      output.write(reqStr.toString().getBytes());

      TestUtil.logMsg("Writing first chunk");
      writeChunk(output, "Hello");
 
      TestUtil.logMsg("Writing second chunk");
      writeChunk(output, "World");

      TestUtil.logMsg("Consuming the response from the server");

      // Consume the response from the server
      input = s.getInputStream();
      int len = -1;
      byte b[] = new byte[1024];            
      boolean receivedFirstMessage = false;
      boolean receivedSecondMessage = false;
      boolean receivedThirdMessage = false;
      StringBuilder sb = new StringBuilder();
      while ((len = input.read(b)) != -1) {
        String line = new String(b, 0, len);
        sb.append(line);
        TestUtil.logMsg("==============Read from server:" + CRLF + sb + CRLF);
        if (passed1 = ServletTestUtil.compareString(EXPECTED_RESPONSE1, sb.toString())) {
          TestUtil.logMsg("==============Received first expected response!" + CRLF);
          receivedFirstMessage = true;
        }
		if (passed2 = ServletTestUtil.compareString(EXPECTED_RESPONSE2, sb.toString())) {
          TestUtil.logMsg("==============Received second expected response!" + CRLF);
          receivedSecondMessage = true;
        }
        if (passed3 = ServletTestUtil.compareString(EXPECTED_RESPONSE3, sb.toString())) {
          TestUtil.logMsg("==============Received third expected response!" + CRLF);
          receivedThirdMessage = true;
        }
        TestUtil.logMsg("receivedFirstMessage : " + receivedFirstMessage);
        TestUtil.logMsg("receivedSecondMessage : " + receivedSecondMessage);
        TestUtil.logMsg("receivedThirdMessage : " + receivedThirdMessage);
        if (receivedFirstMessage &&  receivedSecondMessage && receivedThirdMessage) {
          break;
        }
      }
    } catch (IOException ex2) {
      TestUtil.logErr("exception caught: " + ex2.getMessage(), ex2);
    } catch (Exception ex) {
      TestUtil.logErr(ex.getMessage(), ex);
    } finally

    {
      try {
        if (input != null) {
          TestUtil.logMsg("Closing input...");
          input.close();
          TestUtil.logMsg("Input closed.");
        }
      } catch (Exception ex) {
        TestUtil.logErr("Failed to close input:" + ex.getMessage(), ex);
      }

      try {
        if (output != null) {
          TestUtil.logMsg("Closing output...");
          output.close();
          TestUtil.logMsg("Output closed .");
        }
      } catch (Exception ex) {
        TestUtil.logErr("Failed to close output:" + ex.getMessage(), ex);
      }

      try {
        if (s != null) {
          TestUtil.logMsg("Closing socket..." + CRLF);
          s.close();
          TestUtil.logMsg("Socked closed.");
        }
      } catch (Exception ex) {
        TestUtil.logErr("Failed to close socket:" + ex.getMessage(), ex);
      }
    }

    if (!passed1 || !passed2 || !passed3) {
      throw new Exception("Test Failed. ");
    }
  }

  private static void writeChunk(OutputStream out, String data)
      throws IOException {
    if (data != null) {
      out.write(data.getBytes());
    }
    out.flush();
  }
}
