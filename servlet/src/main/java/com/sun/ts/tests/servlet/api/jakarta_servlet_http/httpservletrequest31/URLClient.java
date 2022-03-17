/*
 * Copyright (c) 2012, 2020 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.servlet.api.jakarta_servlet_http.httpservletrequest31;

import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.tests.servlet.common.request.HttpRequestClient;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ArquillianExtension.class)
public class URLClient extends HttpRequestClient {

  private static final String CONTEXT_ROOT = "/servlet_jsh_httpservletrequest31_web";

  @ArquillianResource
  private URL url;

  @BeforeEach
  public void setup() throws Exception {
    setServletName("TestServlet");
    String ctxRoot = url.getPath();
    setContextRoot(ctxRoot.endsWith("/")?ctxRoot.substring(0, ctxRoot.length()-1):ctxRoot);
    Properties properties = new Properties();
    properties.put(SERVLETHOSTPROP, url.getHost());
    properties.put(SERVLETPORTPROP, Integer.toString(url.getPort()));
    // TODO do we really need this??
    properties.put(TSHOME, Files.createTempDirectory("tshome").toString());
    setup(null, properties);
  }


  /**
   * Deployment for the test
   */
  @Deployment(testable = false)
  public static WebArchive getTestArchive() throws Exception {
    return ShrinkWrap.create(WebArchive.class, "client-test.war")
            .setWebXML(URLClient.class.getResource("servlet_jsh_httpservletrequest31_web.xml"));
  }


  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */
  /*
   * @testName: changeSessionIDTest
   *
   * @assertion_ids: Servlet:JAVADOC:929.1;
   *
   * @test_Strategy: Send an HttpServletRequest to server; Verify that
   * request.changeSessionId() throws IllegalStateException when it is called
   * without a session;
   */
}
