/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.servlet.api.jakarta_servlet_http.httpservletresponse30;

import com.sun.ts.tests.servlet.common.response.HttpResponseClient;
import com.sun.ts.tests.servlet.common.servlets.CommonServlets;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.BeforeEach;

public class URLClient extends HttpResponseClient {

  @BeforeEach
  public void setupServletName() throws Exception {
    setServletName("TestServlet");
  }

  /**
   * Deployment for the test
   */
  @Deployment(testable = false)
  public static WebArchive getTestArchive() throws Exception {
    return ShrinkWrap.create(WebArchive.class, "servlet_jsh_httpservletresponse30_web.war")
            .addAsLibraries(CommonServlets.getCommonServletsArchive())
            .setWebXML(URLClient.class.getResource("servlet_jsh_httpservletresponse30_web.xml"));
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run test */
  /*
   * @testName: getHeadersTest
   *
   * @assertion_ids: Servlet:JAVADOC:523; Servlet:JAVADOC:525;
   * Servlet:JAVADOC:779;
   *
   * @test_Strategy: Create a Servlet, In the servlet, set a header value; then
   * add multiple values to it; verify that getHeaders(String) works properly
   */

  /*
   * @testName: getHeaderTest
   *
   * @assertion_ids: Servlet:JAVADOC:523; Servlet:JAVADOC:523;
   * Servlet:JAVADOC:777;
   *
   * @test_Strategy: Create a Servlet, In the servlet, set a header value; then
   * add multiple values to it; verify that getHeader(String) works properly
   */

  /*
   * @testName: getHeaderNamesTest
   *
   * @assertion_ids: Servlet:JAVADOC:520; Servlet:JAVADOC:522;
   * Servlet:JAVADOC:523; Servlet:JAVADOC:525; Servlet:JAVADOC:526;
   * Servlet:JAVADOC:527; Servlet:JAVADOC:778;
   *
   * @test_Strategy: Create a Servlet, In the servlet, set multiuple header
   * values using: #setHeader, #addHeader, #setDateHeader, #addDateHeader,
   * #setIntHeader, and #addIntHeader, verify that getHeaderNames() works
   * properly
   */

  /*
   * @testName: getStatusTest
   *
   * @assertion_ids: Servlet:JAVADOC:780;
   *
   * @test_Strategy: Create a Servlet, In the servlet, set a status value;
   * verify that getStatus() works properly
   */
}
