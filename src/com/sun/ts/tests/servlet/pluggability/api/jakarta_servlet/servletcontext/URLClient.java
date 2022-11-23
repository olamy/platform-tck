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
package com.sun.ts.tests.servlet.pluggability.api.jakarta_servlet.servletcontext;

import com.sun.javatest.Status;
import com.sun.ts.tests.servlet.common.client.AbstractUrlClient;
import java.io.PrintWriter;

public class URLClient extends AbstractUrlClient {

    /**
     * Entry point for different-VM execution. It should delegate to method
     * run(String[], PrintWriter, PrintWriter), and this method should not contain
     * any test configuration.
     */
    public static void main(String[] args) {
        URLClient theTests = new URLClient();
        Status s = theTests.run(args, new PrintWriter(System.out), new PrintWriter(System.err));
        s.exit();
    }

    /**
     * Entry point for same-VM execution. In different-VM execution, the main
     * method delegates to this method.
     */
    public Status run(String args[], PrintWriter out, PrintWriter err) {

        setContextRoot("/servlet_plu_servletcontext_web");
        setServletName("TestServlet");

        return super.run(args, out, err);
    }

    /*
     * @class.setup_props: webServerHost; webServerPort; ts_home;
     */
    /* Run test */
    /*
     * @testName: GetServletTempDirTest
     *
     * @assertion_ids: Servlet:JAVADOC:124; Servlet:JAVADOC:258;
     * Servlet:SPEC:19.1;
     *
     * @test_Strategy: Servlet verify's that the value from the
     * ServletContext.getAttribute("jakarta.servlet.temp.dir") returns non-null
     * value that points an exsiting directory.
     */
    public void GetServletTempDirTest() throws Fault {
        TEST_PROPS.setProperty(APITEST, "getServletTempDir");
        invoke();
    }

    /*
     * @testName: GetMajorVersionTest
     *
     * @assertion_ids: Servlet:JAVADOC:124; Servlet:JAVADOC:258;
     * Servlet:JAVADOC:220;
     *
     * @test_Strategy: Test the ServletContext.getMajorVersion() for this servlet
     * itself
     */
    public void GetMajorVersionTest() throws Fault {
        TEST_PROPS.setProperty(APITEST, "getMajorVersion");
        invoke();
    }

    /*
     * @testName: GetMinorVersionTest
     *
     * @assertion_ids: Servlet:JAVADOC:124; Servlet:JAVADOC:258;
     * Servlet:JAVADOC:221;
     *
     * @test_Strategy: Test the ServletContext.getMinorVersion() for this servlet
     * itself
     */
    public void GetMinorVersionTest() throws Fault {
        TEST_PROPS.setProperty(APITEST, "getMinorVersion");
        invoke();
    }

    /*
     * @testName: GetMimeTypeTest
     *
     * @assertion_ids: Servlet:JAVADOC:124; Servlet:JAVADOC:258;
     * Servlet:JAVADOC:222;
     *
     * @test_Strategy: Test the ServletContext.getMimeType() for this servlet
     * itself
     */
    public void GetMimeTypeTest() throws Fault {
        TEST_PROPS.setProperty(APITEST, "getMimeType");
        invoke();
    }

    /*
     * @testName: GetMimeType_1Test
     *
     * @assertion_ids: Servlet:JAVADOC:124; Servlet:JAVADOC:258;
     * Servlet:JAVADOC:223;
     *
     * @test_Strategy: A negative test for getMimeType(). Test the
     * ServletContext.getMimeType() for this servlet itself
     */
    public void GetMimeType_1Test() throws Fault {
        TEST_PROPS.setProperty(APITEST, "getMimeType_1");
        invoke();
    }

    /*
     * @testName: GetRealPathTest
     *
     * @assertion_ids: Servlet:SPEC:90; Servlet:SPEC:92.2; Servlet:JAVADOC:124;
     * Servlet:JAVADOC:258; Servlet:JAVADOC:241;
     *
     * @test_Strategy: Test the ServletContext.getRealPath() for this servlet
     * itself
     */
    public void GetRealPathTest() throws Fault {
        TEST_PROPS.setProperty(APITEST, "getRealPath");
        invoke();
    }

    /*
     * @testName: GetResourceAsStream_1Test
     *
     * @assertion_ids: Servlet:SPEC:90; Servlet:SPEC:92.2; Servlet:JAVADOC:124;
     * Servlet:JAVADOC:258; Servlet:JAVADOC:229;
     *
     * @test_Strategy: A negative test for getResourceAsStream() method
     */
    public void GetResourceAsStream_1Test() throws Fault {
        TEST_PROPS.setProperty(APITEST, "getResourceAsStream_1");
        invoke();
    }

    /*
     * @testName: GetResource_1Test
     *
     * @assertion_ids: Servlet:JAVADOC:124; Servlet:JAVADOC:258;
     * Servlet:JAVADOC:225;
     *
     * @test_Strategy: A negative test for ServletContext.getResource(String)
     * method
     */
    public void GetResource_1Test() throws Fault {
        TEST_PROPS.setProperty(APITEST, "getResource_1");
        invoke();
    }

    /*
     * @testName: GetResource_2Test
     *
     * @assertion_ids: Servlet:JAVADOC:124; Servlet:JAVADOC:258;
     * Servlet:JAVADOC:227;
     *
     * @test_Strategy: A negative test for ServletContext.getResource(String path)
     * if path does not start with /, MalformedURLException should be thrown
     */
    public void GetResource_2Test() throws Fault {
        TEST_PROPS.setProperty(SEARCH_STRING, "");
        TEST_PROPS.setProperty(APITEST, "getResource_2");
        invoke();
    }

    /*
     * @testName: GetServerInfoTest
     *
     * @assertion_ids: Servlet:JAVADOC:124; Servlet:JAVADOC:258;
     * Servlet:JAVADOC:242;
     *
     * @test_Strategy: Test for ServletContext.getServerInfo() method
     */
    public void GetServerInfoTest() throws Fault {
        TEST_PROPS.setProperty(APITEST, "getServerInfo");
        invoke();
    }

    /*
     * @testName: ServletContextGetAttributeTest
     *
     * @assertion_ids: Servlet:JAVADOC:124; Servlet:JAVADOC:258;
     * Servlet:JAVADOC:250; Servlet:JAVADOC:247;
     *
     * @test_Strategy: Try to get the attributes for this servlet itself
     */
    public void ServletContextGetAttributeTest() throws Fault {
        TEST_PROPS.setProperty(APITEST, "servletContextGetAttribute");
        invoke();
    }

    /*
     * @testName: ServletContextGetAttribute_1Test
     *
     * @assertion_ids: Servlet:JAVADOC:124; Servlet:JAVADOC:258;
     * Servlet:JAVADOC:248;
     *
     * @test_Strategy: A negative test for ServletContext.getAttribute(). Test for
     * null attribute values for this servlet itself
     */
    public void ServletContextGetAttribute_1Test() throws Fault {
        TEST_PROPS.setProperty(APITEST, "servletContextGetAttribute_1");
        invoke();
    }

    /*
     * @testName: ServletContextGetContextTest
     *
     * @assertion_ids: Servlet:JAVADOC:124; Servlet:JAVADOC:258;
     * Servlet:JAVADOC:219;
     *
     * @test_Strategy: Test for ServletContext object for this servlet itself
     */
    public void ServletContextGetContextTest() throws Fault {
        TEST_PROPS.setProperty(APITEST, "servletContextGetContext");
        invoke();
    }

    /*
     * @testName: ServletContextGetInitParameterNamesTest
     *
     * @assertion_ids: Servlet:JAVADOC:124; Servlet:JAVADOC:258;
     * Servlet:JAVADOC:245;
     *
     * @test_Strategy: Test the ServletContext.getInitParameterNames() for this
     * servlet itself
     */
    public void ServletContextGetInitParameterNamesTest() throws Fault {
        TEST_PROPS.setProperty(APITEST, "servletContextGetInitParameterNames");
        invoke();
    }

    /*
     * @testName: ServletContextGetInitParameterTest
     *
     * @assertion_ids: Servlet:JAVADOC:124; Servlet:JAVADOC:258;
     * Servlet:JAVADOC:243;
     *
     * @test_Strategy: Test the ServletContext.getInitParameter(String) for this
     * servlet itself
     */
    public void ServletContextGetInitParameterTest() throws Fault {
        TEST_PROPS.setProperty(APITEST, "servletContextGetInitParameter");
        invoke();
    }

    /*
     * @testName: ServletContextGetInitParameterTestNull
     *
     * @assertion_ids: Servlet:JAVADOC:125; Servlet:JAVADOC:244;
     *
     * @test_Strategy: Test the ServletContext.getInitParameter(String name)
     * returns null when name(nothing_is_set_here_negative_compatibility_test) is
     * not set
     */
    public void ServletContextGetInitParameterTestNull() throws Fault {
        TEST_PROPS.setProperty(APITEST, "servletContextGetInitParameterNull");
        invoke();
    }

    /*
     * @testName: ServletContextRemoveAttributeTest
     *
     * @assertion_ids: Servlet:JAVADOC:124; Servlet:JAVADOC:258;
     * Servlet:JAVADOC:250; Servlet:JAVADOC:254; Servlet:JAVADOC:248;
     *
     * @test_Strategy: Test for ServletContext.removeAttribute() method
     */
    public void ServletContextRemoveAttributeTest() throws Fault {
        TEST_PROPS.setProperty(APITEST, "servletContextRemoveAttribute");
        invoke();
    }

    /*
     * @testName: ServletContextSetAttributeTest
     *
     * @assertion_ids: Servlet:JAVADOC:124; Servlet:JAVADOC:258;
     * Servlet:JAVADOC:250; Servlet:JAVADOC:247;
     *
     * @test_Strategy: Test for ServletContext.setAttribute() method
     */
    public void ServletContextSetAttributeTest() throws Fault {
        TEST_PROPS.setProperty(APITEST, "servletContextSetAttribute");
        invoke();
    }

    /*
     * @testName: ServletContextSetAttribute_1Test
     *
     * @assertion_ids: Servlet:JAVADOC:124; Servlet:JAVADOC:258;
     * Servlet:JAVADOC:250; Servlet:JAVADOC:251; Servlet:JAVADOC:247;
     *
     * @test_Strategy: Test for ServletContext.setAttribute() method Call
     * ServletContext.setAttribute(String Attribute, Object value) twice with the
     * same Attribute, verify that second value replace the first Attribute value.
     */
    public void ServletContextSetAttribute_1Test() throws Fault {
        TEST_PROPS.setProperty(APITEST, "servletContextSetAttribute_1");
        invoke();
    }

    /*
     * @testName: ServletContextSetAttribute_2Test
     *
     * @assertion_ids: Servlet:JAVADOC:124; Servlet:JAVADOC:258;
     * Servlet:JAVADOC:250; Servlet:JAVADOC:253; Servlet:JAVADOC:248;
     *
     * @test_Strategy: Test for ServletContext.setAttribute() method Set Attribute
     * to null and verify getAttribute return null.
     */
    public void ServletContextSetAttribute_2Test() throws Fault {
        TEST_PROPS.setProperty(APITEST, "servletContextSetAttribute_2");
        invoke();
    }

    /*
     * @testName: ServletContextGetAttributeNamesTest
     *
     * @assertion_ids: Servlet:JAVADOC:124; Servlet:JAVADOC:258;
     * Servlet:JAVADOC:250; Servlet:JAVADOC:249;
     *
     * @test_Strategy: Servlet retrieves attributes which it set itself
     */
    public void ServletContextGetAttributeNamesTest() throws Fault {
        TEST_PROPS.setProperty(APITEST, "servletContextGetAttributeNames");
        invoke();
    }

    /*
     * @testName: ServletContextGetRequestDispatcherTest
     *
     * @assertion_ids: Servlet:JAVADOC:124; Servlet:JAVADOC:258;
     * Servlet:JAVADOC:230;
     *
     * @test_Strategy: Test the ServletContext.getRequestDispatcher(String) for
     * this servlet itself
     */
    public void ServletContextGetRequestDispatcherTest() throws Fault {
        TEST_PROPS.setProperty(APITEST, "servletContextGetRequestDispatcher");
        invoke();
    }

    /*
     * @testName: GetNamedDispatcherTest
     *
     * @assertion_ids: Servlet:JAVADOC:124; Servlet:JAVADOC:258;
     * Servlet:JAVADOC:232;
     *
     * @test_Strategy: Servlet verify's that the result from the
     * getNamedDispatcher call and the getServletName call are the same for the
     * servlet.
     */
    public void GetNamedDispatcherTest() throws Fault {
        TEST_PROPS.setProperty(APITEST, "getNamedDispatcher");
        invoke();
    }

    /*
     * @testName: GetNamedDispatcher_1Test
     *
     * @assertion_ids: Servlet:JAVADOC:124; Servlet:JAVADOC:258;
     * Servlet:JAVADOC:233;
     *
     * @test_Strategy: Servlet verify's that the result from the
     * getNamedDispatcher call return null with non-existent path. Negative test.
     */
    public void GetNamedDispatcher_1Test() throws Fault {
        TEST_PROPS.setProperty(APITEST, "getNamedDispatcher_1");
        invoke();
    }
}
