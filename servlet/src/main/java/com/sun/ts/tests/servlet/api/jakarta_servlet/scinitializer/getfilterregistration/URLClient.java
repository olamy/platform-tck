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
 * $Id:$
 */
package com.sun.ts.tests.servlet.api.jakarta_servlet.scinitializer.getfilterregistration;

import com.sun.ts.tests.servlet.common.client.AbstractUrlClient;
import com.sun.ts.tests.servlet.common.util.ResourcesUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

public class URLClient extends AbstractUrlClient {

  @BeforeEach
  public void setupServletName() throws Exception {
    setServletName("TestServlet");
  }

  /**
   * Deployment for the test
   */
  @Deployment(testable = false)
  public static WebArchive getTestArchive() throws Exception {
    Path tmpJar = ResourcesUtils.createTempJarWihOneEntry("META-INF/services/jakarta.servlet.ServletContainerInitializer",
            URLClient.class.getResource("jakarta.servlet.ServletContainerInitializer"));
    WebArchive webArchive = ShrinkWrap.create(WebArchive.class, "client-test.war")
            .addAsLibraries(tmpJar.toFile())
            .setWebXML(URLClient.class.getResource("servlet_sci_getfilterregistration_web.xml"));
    return webArchive;
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */
  /* Run test */
  /*
   * @testName: getFilterRegistrationTest
   *
   * @assertion_ids: Servlet:JAVADOC:688.1;
   *
   * @test_Strategy: Create a ServletContextInitializer, in which, Add a
   * ServletContextListener instance using ServletContext.addListener; in the
   * ServletContextListener call ServletContext.getFilterRegistration(String)
   * Verify that UnsupportedOperationException is thrown.
   */
  @Test
  public void getFilterRegistrationTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "getFilterRegistrationTest");
    invoke();
  }
}
