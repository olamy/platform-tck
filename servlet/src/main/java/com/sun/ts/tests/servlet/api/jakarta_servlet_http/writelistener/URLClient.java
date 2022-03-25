/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.servlet.api.jakarta_servlet_http.writelistener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.servlet.common.client.AbstractUrlClient;
import com.sun.ts.tests.servlet.common.util.ServletTestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class URLClient extends AbstractUrlClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(URLClient.class);

  // TOFIX

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */
  /* Run test */
  /*
   * @testName: nioOutputTest
   *
   * @assertion_ids: Servlet:JAVADOC:911; Servlet:JAVADOC:916;
   * Servlet:JAVADOC:917; Servlet:JAVADOC:582; Servlet:JAVADOC:609;
   *
   * @test_Strategy: Create a Servlet TestServlet which supports async; Create a
   * Writeistener; From Servlet, sends one batch of messages use stream; Verify
   * all message received by client; Verify WriteListener works accordingly
   */
  public void nioOutputTest() throws Exception {
    boolean passed = true;
    String testName = "nioOutputTest";
    String EXPECTED_RESPONSE = "=onWritePossible";

    String requestUrl = getContextRoot() + "/" + getServletName() + "?testname="
        + testName;

    try {

      URL url = new URL(getURLString("http", _hostname, _port, requestUrl));

      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      LOGGER.trace("======= Connecting {}", url.toExternalForm());
      conn.setChunkedStreamingMode(5);
      conn.setDoOutput(true);
      LOGGER.trace("======= Header {}", conn);
      conn.connect();

      try (BufferedReader input = new BufferedReader(
              new InputStreamReader(conn.getInputStream()))) {
        String line = null;
        StringBuilder message_received = new StringBuilder();

        while ((line = input.readLine()) != null) {
          LOGGER.trace("======= message received: " + line);
          message_received.append(line);
        }
        passed = ServletTestUtil.compareString(EXPECTED_RESPONSE,
            message_received.toString());

      }
    } catch (Exception ex) {
      passed = false;
      LOGGER.error("Test" + ex.getMessage(), ex);
    }

    if (!passed) {
      throw new Exception("Test Failed.");
    }
  }
}
