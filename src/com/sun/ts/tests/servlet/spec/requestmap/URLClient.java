/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.servlet.spec.requestmap;

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

        setContextRoot("/servlet_js_requestmap_web");

        return super.run(args, out, err);
    }

    /*
     * @class.setup_props: webServerHost; webServerPort; ts_home;
     */

    /* Run test */

    /*
     * @testName: longestPathMatchTest
     *
     * @assertion_ids: Servlet:SPEC:134.2; Servlet:SPEC:134.5; Servlet:SPEC:133;
     *
     * @test_Strategy: 1. Create servlets TestServlet1 with URL /foo/bar/*,
     * TestServlet2 with URL /foo/baR/*, TestServlet3 with URL /TestServlet3,
     * TestServlet4 with URL *.bop, TestServlet5 with URL /foo/bar/TestServlet5.
     * 2. Send request with path /foo/bar/index.html. 3. Verify that TestServlet1
     * is invoked based on Servlet Spec(11.1) that it has longest path-prefix
     * match and it is the default Servlet at the path.
     */

    public void longestPathMatchTest() throws Fault {
        TEST_PROPS.setProperty(SEARCH_STRING, "TestServlet1");
        TEST_PROPS.setProperty(APITEST, "foo/bar/index.html");
        invoke();
    }

    /*
     * @testName: longestPathMatchTest1
     *
     * @assertion_ids: Servlet:SPEC:134.2; Servlet:SPEC:134.5; Servlet:SPEC:133;
     *
     * @test_Strategy: 1. Create servlets TestServlet1 with URL /foo/bar/*,
     * TestServlet2 with URL /foo/baR/*, TestServlet3 with URL /TestServlet3,
     * TestServlet4 with URL *.bop, TestServlet5 with URL /foo/bar/TestServlet5.
     * 2. Send request with path /foo/bar. 3. Verify that TestServlet1 is invoked
     * based on Servlet Spec(11.1) that it has longest path-prefix match and it is
     * the default Servlet at the path.
     */

    public void longestPathMatchTest1() throws Fault {
        TEST_PROPS.setProperty(SEARCH_STRING, "TestServlet1");
        TEST_PROPS.setProperty(APITEST, "foo/bar");
        invoke();
    }

    /*
     * @testName: longestPathMatchTest2
     *
     * @assertion_ids: Servlet:SPEC:134.2; Servlet:SPEC:134.5; Servlet:SPEC:133;
     *
     * @test_Strategy: 1. Create servlets TestServlet1 with URL /foo/bar/*,
     * TestServlet2 with URL /foo/baR/*, TestServlet3 with URL /TestServlet3,
     * TestServlet4 with URL *.bop, TestServlet5 with URL /foo/bar/TestServlet5.
     * 2. Send request with path /foo/baR/TestServlet5. 3. Verify that
     * TestServlet2 is invoked based on Servlet Spec(11.1) that it has longest
     * path-prefix match and it is the default Servlet at the path.
     */

    public void longestPathMatchTest2() throws Fault {
        TEST_PROPS.setProperty(SEARCH_STRING, "TestServlet2");
        TEST_PROPS.setProperty(APITEST, "foo/baR/TestServlet5");
        invoke();
    }

    /*
     * @testName: exactMatchTest
     *
     * @assertion_ids: Servlet:SPEC:134.1;
     *
     * @test_Strategy: 1. Create servlets TestServlet1 with URL /foo/bar/*,
     * TestServlet2 with URL /foo/baR/*, TestServlet3 with URL /TestServlet3,
     * TestServlet4 with URL *.bop, TestServlet5 with URL /foo/bar/TestServlet5.
     * 2. Send request with path /TestServlet3. 3. Verify that TestServlet3 is
     * invoked based on Servlet Spec(11.1) that it has exact match.
     */

    public void exactMatchTest() throws Fault {
        TEST_PROPS.setProperty(SEARCH_STRING, "TestServlet3");
        TEST_PROPS.setProperty(APITEST, "TestServlet3");
        invoke();
    }

    /*
     * @testName: exactMatchTest1
     *
     * @assertion_ids: Servlet:SPEC:134.1; Servlet:SPEC:134.5;
     *
     * @test_Strategy: 1. Create servlets TestServlet1 with URL /foo/bar/*,
     * TestServlet2 with URL /foo/baR/*, TestServlet3 with URL /TestServlet3,
     * TestServlet4 with URL *.bop, TestServlet5 with URL /foo/bar/TestServlet5.
     * 2. Send request with path /foo/bar/TestServlet5. 3. Verify that
     * TestServlet5 is invoked based on Servlet Spec(11.1) that it has exact
     * match.
     */

    public void exactMatchTest1() throws Fault {
        TEST_PROPS.setProperty(SEARCH_STRING, "TestServlet5");
        TEST_PROPS.setProperty(APITEST, "foo/bar/TestServlet5");
        invoke();
    }

    /*
     * @testName: extMatchTest
     *
     * @assertion_ids: Servlet:SPEC:134.3;
     *
     * @test_Strategy: 1. Create servlets TestServlet1 with URL /foo/bar/*,
     * TestServlet2 with URL /foo/baR/*, TestServlet3 with URL /TestServlet3,
     * TestServlet4 with URL *.bop, TestServlet5 with URL /foo/bar/TestServlet5.
     * 2. Send request with path /TestServlet3/racecar.bop 3. Verify that
     * TestServlet4 is invoked based on Servlet Spec(11.1) that it has the
     * extension match.
     */

    public void extMatchTest() throws Fault {
        TEST_PROPS.setProperty(SEARCH_STRING, "TestServlet4");
        TEST_PROPS.setProperty(APITEST, "TestServlet3/racecar.bop");
        invoke();
    }

    /*
     * @testName: extMatchTest1
     *
     * @assertion_ids: Servlet:SPEC:134.3;
     *
     * @test_Strategy: 1. Create servlets TestServlet1 with URL /foo/bar/*,
     * TestServlet2 with URL /foo/baR/*, TestServlet3 with URL /TestServlet3,
     * TestServlet4 with URL *.bop, TestServlet5 with URL /foo/bar/TestServlet5.
     * 2. Send request with path /index.bop 3. Verify that TestServlet4 is invoked
     * based on Servlet Spec(11.1) that it has the extension match.
     */

    public void extMatchTest1() throws Fault {
        TEST_PROPS.setProperty(SEARCH_STRING, "TestServlet4");
        TEST_PROPS.setProperty(APITEST, "index.bop");
        invoke();
    }

    /*
     * @testName: notFoundTest1
     *
     * @assertion_ids: Servlet:SPEC:134.4;
     *
     * @test_Strategy: 1. Create servlets TestServlet1 with URL /foo/bar/*,
     * TestServlet2 with URL /foo/baR/*, TestServlet3 with URL /TestServlet3,
     * TestServlet4 with URL *.bop, TestServlet5 with URL /foo/bar/TestServlet5.
     * 2. Send request with path /test/foo/bar/xxx 3. Verify that no match is
     * found and 404 should be returned.
     */

    public void notFoundTest1() throws Fault {
        TEST_PROPS.setProperty(STATUS_CODE, NOT_FOUND);
        TEST_PROPS.setProperty(REQUEST, "GET " + getContextRoot() + "/test/foo/bar/xxx" + " HTTP/1.1");
        invoke();
    }
}
