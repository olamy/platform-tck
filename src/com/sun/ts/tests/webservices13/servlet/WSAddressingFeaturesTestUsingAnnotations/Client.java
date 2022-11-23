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

package com.sun.ts.tests.webservices13.servlet.WSAddressingFeaturesTestUsingAnnotations;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.tests.jaxws.common.*;
import jakarta.ejb.EJB;
import jakarta.xml.ws.*;
import jakarta.xml.ws.soap.*;
import java.net.*;
import java.util.Properties;

public class Client extends EETest {

    // The webserver defaults (overidden by harness properties)
    private static final String PROTOCOL = "http";

    private static final String HOSTNAME = "localhost";

    private static final int PORTNUM = 8000;

    // The webserver host and port property names (harness properties)
    private static final String WEBSERVERHOSTPROP = "webServerHost";

    private static final String WEBSERVERPORTPROP = "webServerPort";

    private TSURL ctsurl = new TSURL();

    private Properties props = null;

    private String hostname = HOSTNAME;

    private int portnum = PORTNUM;

    // URL properties used by the test
    private URL url = null;

    private URLConnection urlConn = null;

    private String SERVLET = "/WSAddressingFeaturesTestUsingAnnotationsClnt_web/ServletTest";

    @Addressing
    @WebServiceRef(name = "service/wsaddrfeaturestestusingannotationsdefaultechoport", value = EchoService.class)
    static Echo defaultEchoPort = null;

    @Addressing(enabled = true)
    @WebServiceRef(name = "service/wsaddrfeaturestestusingannotationsenabledechoport", value = EchoService.class)
    static Echo enabledEchoPort = null;

    @Addressing(enabled = true, required = true)
    @WebServiceRef(name = "service/wsaddrfeaturestestusingannotationsrequiredechoport", value = EchoService.class)
    static Echo requiredEchoPort = null;

    @Addressing(enabled = false)
    @WebServiceRef(name = "service/wsaddrfeaturestestusingannotationsdisabledechoport", value = EchoService.class)
    static Echo disabledEchoPort = null;

    @Addressing
    @WebServiceRef(name = "service/wsaddrfeaturestestusingannotationsdefaultecho2port", value = EchoService.class)
    static Echo2 defaultEcho2Port = null;

    @Addressing(enabled = true)
    @WebServiceRef(name = "service/wsaddrfeaturestestusingannotationsenabledecho2port", value = EchoService.class)
    static Echo2 enabledEcho2Port = null;

    @Addressing(enabled = true, required = true)
    @WebServiceRef(name = "service/wsaddrfeaturestestusingannotationsrequiredecho2port", value = EchoService.class)
    static Echo2 requiredEcho2Port = null;

    @Addressing(enabled = false)
    @WebServiceRef(name = "service/wsaddrfeaturestestusingannotationsdisabledecho2port", value = EchoService.class)
    static Echo2 disabledEcho2Port = null;

    @WebServiceRef(name = "service/wsaddrfeaturestestusingannotationsservice")
    static EchoService service = null;

    @EJB(name = "ejb/WSAddressingFeaturesTestUsingAnnotationsClntBean")
    static EjbClientIF ejbclient;

    public static void main(String[] args) {
        Client theTests = new Client();
        Status s = theTests.run(args, System.out, System.err);
        s.exit();
    }

    /* Test setup */

    /*
     * @class.setup_props: webServerHost; webServerPort;
     */
    public void setup(String[] args, Properties p) throws Fault {
        props = p;
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
                TestUtil.printStackTrace(e);
                pass = false;
            }
            TestUtil.logMsg("AppClient DEBUG: ejbclient=" + ejbclient);
            TestUtil.logMsg("AppClient DEBUG: service=" + service);
            TestUtil.logMsg("AppClient DEBUG: defaultEchoPort=" + defaultEchoPort);
            TestUtil.logMsg("AppClient DEBUG: enabledEchoPort=" + enabledEchoPort);
            TestUtil.logMsg("AppClient DEBUG: requiredEchoPort=" + requiredEchoPort);
            TestUtil.logMsg("AppClient DEBUG: disabledEchoPort=" + disabledEchoPort);
            TestUtil.logMsg("AppClient DEBUG: defaultEcho2Port=" + defaultEcho2Port);
            TestUtil.logMsg("AppClient DEBUG: enabledEcho2Port=" + enabledEcho2Port);
            TestUtil.logMsg("AppClient DEBUG: requiredEcho2Port=" + requiredEcho2Port);
            TestUtil.logMsg("AppClient DEBUG: disabledEcho2Port=" + disabledEcho2Port);
        } catch (Exception e) {
            throw new Fault("setup failed:", e);
        }
        if (!pass) {
            TestUtil.logErr("Please specify host & port of web server " + "in config properties: " + WEBSERVERHOSTPROP
                    + ", " + WEBSERVERPORTPROP);
            throw new Fault("setup failed:");
        }
        if (service == null
                || ejbclient == null
                || defaultEchoPort == null
                || enabledEchoPort == null
                || requiredEchoPort == null
                || disabledEchoPort == null
                || defaultEcho2Port == null
                || enabledEcho2Port == null
                || requiredEcho2Port == null
                || disabledEcho2Port == null) {
            throw new Fault("setup failed: injection failure");
        }
        ejbclient.init(p);
        TestUtil.logMsg("setup ok");
    }

    public void cleanup() throws Fault {
        TestUtil.logMsg("cleanup ok");
    }

    /*
     * @testName: VerifyAddrHeadersExistForRequiredEchoPort
     *
     * @assertion_ids: WS4EE:SPEC:3002; WS4EE:SPEC:3003; WS4EE:SPEC:3005; WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:4015;
     *
     * @test_Strategy: Verify Addressing Headers MUST exist on SOAP request and SOAP response for appclient, servlet, and
     * ejb containers. Addressing enabled/required by client/supported by endpoint.
     */
    public void VerifyAddrHeadersExistForRequiredEchoPort() throws Fault {
        boolean pass = true;

        TestUtil.logMsg("VerifyAddrHeadersExistForRequiredEchoPort");
        try {
            TestUtil.logMsg("-------------------------------");
            TestUtil.logMsg("Test appclient invocation .....");
            TestUtil.logMsg("-------------------------------");
            TestUtil.logMsg("AppClient invoking EchoService echo() method");
            TestUtil.logMsg("Addressing headers MUST be present on the SOAPRequest/SOAPResponse");
            TestUtil.logMsg("VerifyAddrHeadersExistForRequiredEchoPort");
            Holder<String> testName = new Holder("VerifyAddrHeadersExistForRequiredEchoPort");
            requiredEchoPort.echo("Echo from AppClient on requiredEchoPort", testName);
            TestUtil.logMsg("Addressing Headers Exist (Expected)");
        } catch (Exception e) {
            TestUtil.logErr("Addressing Headers DO NOT Exist (Unexpected)");
            pass = false;
        }
        try {
            TestUtil.logMsg("-----------------------------------");
            TestUtil.logMsg("Test servletclient invocation .....");
            TestUtil.logMsg("-----------------------------------");
            url = ctsurl.getURL("http", hostname, portnum, SERVLET);
            TestUtil.logMsg("Servlet URL: " + url);
            props.setProperty("TEST", "VerifyAddrHeadersExistForRequiredEchoPort");
            urlConn = TestUtil.sendPostData(props, url);
            Properties p = TestUtil.getResponseProperties(urlConn);
            String passStr = p.getProperty("TESTRESULT");
            if (passStr.equals("fail")) {
                pass = false;
                TestUtil.logErr("Addressing Headers DO NOT Exist (Unexpected)");
            } else
                TestUtil.logMsg("Addressing Headers Exist (Expected)");
        } catch (Exception e) {
            TestUtil.logErr("Addressing Headers DO NOT Exist (Unexpected)");
            pass = false;
        }
        try {
            TestUtil.logMsg("-------------------------------");
            TestUtil.logMsg("Test ejbclient invocation .....");
            TestUtil.logMsg("-------------------------------");
            String testName = "VerifyAddrHeadersExistForRequiredEchoPort";
            boolean passEjb = ejbclient.echo("Echo from EjbClient on requiredEchoPort", testName);
            if (!passEjb) {
                TestUtil.logErr("Addressing Headers DO NOT Exist (Unexpected)");
                pass = false;
            } else
                TestUtil.logMsg("Addressing Headers Exist (Expected)");
        } catch (Exception e) {
            TestUtil.logErr("Addressing Headers DO NOT Exist (Unexpected)");
            pass = false;
        }
        if (!pass)
            throw new Fault("VerifyAddrHeadersExistForRequiredEchoPort failed");
    }

    /*
     * @testName: VerifyAddrHeadersDoNotExistForDisabledEchoPort
     *
     * @assertion_ids: WS4EE:SPEC:3002; WS4EE:SPEC:3003; WS4EE:SPEC:3005; WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:4015;
     *
     * @test_Strategy: Verify Addressing Headers MUST NOT exist on SOAP request and SOAP response for appclient, servlet,
     * and ejb containers. Addressing disabled by client/supported by endpoint.
     */
    public void VerifyAddrHeadersDoNotExistForDisabledEchoPort() throws Fault {
        boolean pass = true;

        TestUtil.logMsg("VerifyAddrHeadersDoNotExistForDisabledEchoPort");
        try {
            TestUtil.logMsg("-------------------------------");
            TestUtil.logMsg("Test appclient invocation .....");
            TestUtil.logMsg("-------------------------------");
            TestUtil.logMsg("AppClient invoking EchoService echo() method");
            TestUtil.logMsg("Addressing headers MUST NOT be present on the SOAPRequest/SOAPResponse");
            TestUtil.logMsg("VerifyAddrHeadersDoNotExistForDisabledEchoPort");
            Holder<String> testName = new Holder("VerifyAddrHeadersDoNotExistForDisabledEchoPort");
            disabledEchoPort.echo("Echo from AppClient on disabledEchoPort", testName);
            TestUtil.logMsg("Addressing Headers DO NOT Exist (Expected)");
        } catch (Exception e) {
            TestUtil.logErr("Addressing Headers Exist (Unexpected)");
            pass = false;
        }
        try {
            TestUtil.logMsg("-----------------------------------");
            TestUtil.logMsg("Test servletclient invocation .....");
            TestUtil.logMsg("-----------------------------------");
            url = ctsurl.getURL("http", hostname, portnum, SERVLET);
            TestUtil.logMsg("Servlet URL: " + url);
            props.setProperty("TEST", "VerifyAddrHeadersDoNotExistForDisabledEchoPort");
            urlConn = TestUtil.sendPostData(props, url);
            Properties p = TestUtil.getResponseProperties(urlConn);
            String passStr = p.getProperty("TESTRESULT");
            if (passStr.equals("fail")) {
                pass = false;
                TestUtil.logErr("Addressing Headers Exist (Unexpected)");
            } else
                TestUtil.logMsg("Addressing Headers DO NOT Exist (Expected)");
        } catch (Exception e) {
            TestUtil.logErr("Addressing Headers Exist (Unexpected)");
            pass = false;
        }
        try {
            TestUtil.logMsg("-------------------------------");
            TestUtil.logMsg("Test ejbclient invocation .....");
            TestUtil.logMsg("-------------------------------");
            String testName = "VerifyAddrHeadersDoNotExistForDisabledEchoPort";
            boolean passEjb = ejbclient.echo("Echo from EjbClient on disabledEchoPort", testName);
            if (!passEjb) {
                TestUtil.logErr("Addressing Headers Exist (Unexpected)");
                pass = false;
            } else
                TestUtil.logMsg("Addressing Headers DO NOT Exist (Expected)");
        } catch (Exception e) {
            TestUtil.logErr("Addressing Headers Exist (Unexpected)");
            pass = false;
        }
        if (!pass)
            throw new Fault("VerifyAddrHeadersDoNotExistForDisabledEchoPort failed");
    }

    /*
     * @testName: VerifyAddrHeadersMayExistForEnabledEchoPort
     *
     * @assertion_ids: WS4EE:SPEC:3002; WS4EE:SPEC:3003; WS4EE:SPEC:3005; WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:4015;
     *
     * @test_Strategy: Verify Addressing Headers may exist on SOAP request and SOAP response for appclient, servlet, and ejb
     * containers. Addressing enabled/notrequired by client/supported by endpoint.
     */
    public void VerifyAddrHeadersMayExistForEnabledEchoPort() throws Fault {
        boolean pass = true;

        TestUtil.logMsg("VerifyAddrHeadersMayExistForEnabledEchoPort");
        try {
            TestUtil.logMsg("-------------------------------");
            TestUtil.logMsg("Test appclient invocation .....");
            TestUtil.logMsg("-------------------------------");
            TestUtil.logMsg("AppClient invoking EchoService echo() method");
            TestUtil.logMsg("Addressing headers MAY be present on the SOAPRequest/SOAPResponse");
            TestUtil.logMsg("VerifyAddrHeadersMayExistForEnabledEchoPort");
            Holder<String> testName = new Holder("VerifyAddrHeadersMayExistForEnabledEchoPort");
            enabledEchoPort.echo("Echo from AppClient on enabledEchoPort", testName);
        } catch (Exception e) {
            TestUtil.logErr("Unexpected exception: " + e.getMessage());
            pass = false;
        }
        try {
            TestUtil.logMsg("-----------------------------------");
            TestUtil.logMsg("Test servletclient invocation .....");
            TestUtil.logMsg("-----------------------------------");
            url = ctsurl.getURL("http", hostname, portnum, SERVLET);
            TestUtil.logMsg("Servlet URL: " + url);
            props.setProperty("TEST", "VerifyAddrHeadersMayExistForEnabledEchoPort");
            urlConn = TestUtil.sendPostData(props, url);
            Properties p = TestUtil.getResponseProperties(urlConn);
            String passStr = p.getProperty("TESTRESULT");
            if (passStr.equals("fail")) {
                TestUtil.logErr("Unexpected failure occurred");
                pass = false;
            }
        } catch (Exception e) {
            TestUtil.logErr("Unexpected exception: " + e.getMessage());
            pass = false;
        }
        try {
            TestUtil.logMsg("-------------------------------");
            TestUtil.logMsg("Test ejbclient invocation .....");
            TestUtil.logMsg("-------------------------------");
            String testName = "VerifyAddrHeadersMayExistForEnabledEchoPort";
            boolean passEjb = ejbclient.echo("Echo from EjbClient on enabledEchoPort", testName);
            if (!passEjb) {
                TestUtil.logErr("Unexpected failure occurred");
                pass = false;
            }
        } catch (Exception e) {
            TestUtil.logErr("Unexpected exception: " + e.getMessage());
            pass = false;
        }
        if (!pass)
            throw new Fault("VerifyAddrHeadersMayExistForEnabledEchoPort failed");
    }

    /*
     * @testName: VerifyExceptionThrownForRequiredEcho2Port
     *
     * @assertion_ids: WS4EE:SPEC:3002; WS4EE:SPEC:3003; WS4EE:SPEC:3005; WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:4015;
     *
     * @test_Strategy: Negative test case. Verify Exception is thrown back. Addressing enabled/required by client/not
     * supported by endpoint. Expect a WebServiceException to be thrown.
     */
    public void VerifyExceptionThrownForRequiredEcho2Port() throws Fault {
        boolean pass = true;

        TestUtil.logMsg("VerifyExceptionThrownForRequiredEcho2Port");
        try {
            TestUtil.logMsg("-------------------------------");
            TestUtil.logMsg("Test appclient invocation .....");
            TestUtil.logMsg("-------------------------------");
            TestUtil.logMsg("AppClient invoking EchoService echo() method");
            TestUtil.logMsg("Expect a WebServiceException to be thrown back");
            TestUtil.logMsg("VerifyExceptionThrownForRequiredEcho2Port");
            Holder<String> testName = new Holder("VerifyExceptionThrownForRequiredEcho2Port");
            requiredEcho2Port.echo("Echo from AppClient on requiredEcho2Port", testName);
            TestUtil.logErr("WebServiceException was not thrown back");
            pass = false;
        } catch (WebServiceException e) {
            TestUtil.logMsg("Caught expected WebServiceException: " + e.getMessage());
        } catch (Exception e) {
            TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
            pass = false;
        }
        try {
            TestUtil.logMsg("-----------------------------------");
            TestUtil.logMsg("Test servletclient invocation .....");
            TestUtil.logMsg("-----------------------------------");
            url = ctsurl.getURL("http", hostname, portnum, SERVLET);
            TestUtil.logMsg("Servlet URL: " + url);
            props.setProperty("TEST", "VerifyExceptionThrownForRequiredEcho2Port");
            urlConn = TestUtil.sendPostData(props, url);
            Properties p = TestUtil.getResponseProperties(urlConn);
            String passStr = p.getProperty("TESTRESULT");
            if (passStr.equals("fail")) {
                pass = false;
                TestUtil.logErr("WebServiceException was not thrown back");
            } else
                TestUtil.logMsg("Caught expected WebServiceException");
        } catch (Exception e) {
            TestUtil.logMsg("Unexpected Exception: " + e.getMessage());
            pass = false;
        }
        try {
            TestUtil.logMsg("-------------------------------");
            TestUtil.logMsg("Test ejbclient invocation .....");
            TestUtil.logMsg("-------------------------------");
            String testName = "VerifyExceptionThrownForRequiredEcho2Port";
            boolean passEjb = ejbclient.echo("Echo from EjbClient on requiredEcho2Port", testName);
            if (!passEjb) {
                TestUtil.logErr("WebServiceException was not thrown back");
                pass = false;
            } else
                TestUtil.logMsg("Caught expected WebServiceException");
        } catch (Exception e) {
            TestUtil.logMsg("Unexpected Exception: " + e.getMessage());
            pass = false;
        }
        if (!pass)
            throw new Fault("VerifyExceptionThrownForRequiredEcho2Port failed");
    }

    /*
     * @testName: VerifyAddrHeadersDoNotExistForDisabledEcho2Port
     *
     * @assertion_ids: WS4EE:SPEC:3002; WS4EE:SPEC:3003; WS4EE:SPEC:3005; WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:4015;
     *
     * @test_Strategy: Verify Addressing Headers MUST NOT exist on SOAP request and SOAP response for appclient, servlet,
     * and ejb containers. Addressing disabled by client/not supported by endpoint.
     */
    public void VerifyAddrHeadersDoNotExistForDisabledEcho2Port() throws Fault {
        boolean pass = true;

        TestUtil.logMsg("VerifyAddrHeadersDoNotExistForDisabledEcho2Port");
        try {
            TestUtil.logMsg("-------------------------------");
            TestUtil.logMsg("Test appclient invocation .....");
            TestUtil.logMsg("-------------------------------");
            TestUtil.logMsg("AppClient invoking EchoService echo() method");
            TestUtil.logMsg("Addressing headers MUST NOT be present on the SOAPRequest/SOAPResponse");
            TestUtil.logMsg("VerifyAddrHeadersDoNotExistForDisabledEcho2Port");
            Holder<String> testName = new Holder("VerifyAddrHeadersDoNotExistForDisabledEcho2Port");
            disabledEcho2Port.echo("Echo from AppClient on disabledEcho2Port", testName);
            TestUtil.logMsg("Addressing Headers DO NOT Exist (Expected)");
        } catch (Exception e) {
            TestUtil.logErr("Addressing Headers Exist (Unexpected)");
            pass = false;
        }
        try {
            TestUtil.logMsg("-----------------------------------");
            TestUtil.logMsg("Test servletclient invocation .....");
            TestUtil.logMsg("-----------------------------------");
            url = ctsurl.getURL("http", hostname, portnum, SERVLET);
            TestUtil.logMsg("Servlet URL: " + url);
            props.setProperty("TEST", "VerifyAddrHeadersDoNotExistForDisabledEcho2Port");
            urlConn = TestUtil.sendPostData(props, url);
            Properties p = TestUtil.getResponseProperties(urlConn);
            String passStr = p.getProperty("TESTRESULT");
            if (passStr.equals("fail")) {
                pass = false;
                TestUtil.logErr("Addressing Headers Exist (Unexpected)");
            } else
                TestUtil.logMsg("Addressing Headers DO NOT Exist (Expected)");
        } catch (Exception e) {
            TestUtil.logErr("Addressing Headers Exist (Unexpected)");
            pass = false;
        }
        try {
            TestUtil.logMsg("-------------------------------");
            TestUtil.logMsg("Test ejbclient invocation .....");
            TestUtil.logMsg("-------------------------------");
            String testName = "VerifyAddrHeadersDoNotExistForDisabledEcho2Port";
            boolean passEjb = ejbclient.echo("Echo from EjbClient on disabledEcho2Port", testName);
            if (!passEjb) {
                TestUtil.logErr("Addressing Headers Exist (Unexpected)");
                pass = false;
            } else
                TestUtil.logMsg("Addressing Headers DO NOT Exist (Expected)");
        } catch (Exception e) {
            TestUtil.logErr("Addressing Headers Exist (Unexpected)");
            pass = false;
        }
        if (!pass)
            throw new Fault("VerifyAddrHeadersDoNotExistForDisabledEcho2Port failed");
    }

    /*
     * @testName: VerifyAddrHeadersMayExistForEnabledEcho2Port
     *
     * @assertion_ids: WS4EE:SPEC:3002; WS4EE:SPEC:3003; WS4EE:SPEC:3005; WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:4015;
     *
     * @test_Strategy: Verify Addressing Headers may exist on SOAP request and SOAP response for appclient, servlet, and ejb
     * containers. Addressing enabled/notrequired by client/not supported by endpoint.
     */
    public void VerifyAddrHeadersMayExistForEnabledEcho2Port() throws Fault {
        boolean pass = true;

        TestUtil.logMsg("VerifyAddrHeadersMayExistForEnabledEcho2Port");
        try {
            TestUtil.logMsg("-------------------------------");
            TestUtil.logMsg("Test appclient invocation .....");
            TestUtil.logMsg("-------------------------------");
            TestUtil.logMsg("AppClient invoking EchoService echo() method");
            TestUtil.logMsg("Addressing headers MAY be present on the SOAPRequest/SOAPResponse");
            TestUtil.logMsg("VerifyAddrHeadersMayExistForEnabledEcho2Port");
            Holder<String> testName = new Holder("VerifyAddrHeadersMayExistForEnabledEcho2Port");
            enabledEcho2Port.echo("Echo from AppClient on enabledEcho2Port", testName);
        } catch (Exception e) {
            TestUtil.logErr("Unexpected exception: " + e.getMessage());
            pass = false;
        }
        try {
            TestUtil.logMsg("-----------------------------------");
            TestUtil.logMsg("Test servletclient invocation .....");
            TestUtil.logMsg("-----------------------------------");
            url = ctsurl.getURL("http", hostname, portnum, SERVLET);
            TestUtil.logMsg("Servlet URL: " + url);
            props.setProperty("TEST", "VerifyAddrHeadersMayExistForEnabledEcho2Port");
            urlConn = TestUtil.sendPostData(props, url);
            Properties p = TestUtil.getResponseProperties(urlConn);
            String passStr = p.getProperty("TESTRESULT");
            if (passStr.equals("fail")) {
                TestUtil.logErr("Unexpected failure occurred");
                pass = false;
            }
        } catch (Exception e) {
            TestUtil.logErr("Unexpected exception: " + e.getMessage());
            pass = false;
        }
        try {
            TestUtil.logMsg("-------------------------------");
            TestUtil.logMsg("Test ejbclient invocation .....");
            TestUtil.logMsg("-------------------------------");
            String testName = "VerifyAddrHeadersMayExistForEnabledEcho2Port";
            boolean passEjb = ejbclient.echo("Echo from EjbClient on enabledEcho2Port", testName);
            if (!passEjb) {
                TestUtil.logErr("Unexpected failure occurred");
                pass = false;
            }
        } catch (Exception e) {
            TestUtil.logErr("Unexpected exception: " + e.getMessage());
            pass = false;
        }
        if (!pass)
            throw new Fault("VerifyAddrHeadersMayExistForEnabledEcho2Port failed");
    }
}
