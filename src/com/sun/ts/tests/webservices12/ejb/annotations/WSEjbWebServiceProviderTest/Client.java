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

package com.sun.ts.tests.webservices12.ejb.annotations.WSEjbWebServiceProviderTest;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.tests.jaxws.common.*;
import jakarta.xml.ws.*;
import java.io.*;
import java.net.*;
import java.rmi.*;
import java.util.*;

public class Client extends EETest {
    // The webserver defaults (overidden by harness properties)
    private static final String PROTOCOL = "http";

    private static final String HOSTNAME = "localhost";

    private static final int PORTNUM = 8000;

    // The webserver host and port property names (harness properties)
    private static final String WEBSERVERHOSTPROP = "webServerHost";

    private static final String WEBSERVERPORTPROP = "webServerPort";

    private static final String MODEPROP = "platform.mode";

    private TSURL ctsurl = new TSURL();

    private Properties props = null;

    private String hostname = HOSTNAME;

    private int portnum = PORTNUM;

    Hello port = null;

    @WebServiceRef(name = "service/wsejbwebserviceprovidertest")
    static HelloService service;

    private void getPort() throws Exception {
        TestUtil.logMsg("Get wsejbwebserviceprovidertest Service via @WebServiceRef annotation");
        TestUtil.logMsg("Uses name attribute @WebServiceRef(name=\"service/wsejbwebserviceprovidertest\")");
        TestUtil.logMsg("service=" + service);
        TestUtil.logMsg("Get port from service");
        port = (Hello) service.getHelloPort();
        TestUtil.logMsg("port=" + port);
        TestUtil.logMsg("Port obtained");
        JAXWS_Util.dumpTargetEndpointAddress(port);
    }

    public static void main(String[] args) {
        Client theTests = new Client();
        Status s = theTests.run(args, System.out, System.err);
        s.exit();
    }

    /* Test setup */

    /*
     * @class.testArgs: -ap webservices-url-props.dat
     *
     * @class.setup_props: webServerHost; webServerPort; platform.mode;
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
            getPort();
        } catch (Exception e) {
            TestUtil.printStackTrace(e);
            throw new Fault("setup failed:", e);
        }

        if (!pass) {
            TestUtil.logErr("Please specify host & port of web server " + "in config properties: " + WEBSERVERHOSTPROP
                    + ", " + WEBSERVERPORTPROP);
            throw new Fault("setup failed:");
        }
        logMsg("setup ok");
    }

    public void cleanup() throws Fault {
        logMsg("cleanup ok");
    }

    /*
     * @testName: WSEjbWebServiceProviderTestCallSayHello
     *
     * @assertion_ids: WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:5001; WS4EE:SPEC:5002;
     *
     * @test_Strategy: Client imports wsdl from a deployed webservice endpoint, builds the client-side artifacts, then uses
     * the WebServiceRef annotation with name attribute to access and communicate with the deployed webservice endpoint.
     */
    public void WSEjbWebServiceProviderTestCallSayHello() throws Fault {
        TestUtil.logMsg("WSEjbWebServiceProviderTestCallSayHello");
        boolean pass = true;

        for (int i = 0; i < 10; i++) {
            String ret = port.sayHello("ProviderRef Tester loop#" + i + "!");
            if (ret.indexOf("WSEjbWebServiceProvider-SayHello") == -1) {
                TestUtil.logErr("Unexpected greeting " + ret);
                pass = false;
                break;
            } else
                TestUtil.logMsg("Got expected greeting " + ret);
        }
        if (!pass)
            throw new Fault("WSEjbWebServiceProviderTestCallSayHello failed");
    }

    /*
     * @testName: WSEjbWebServiceProviderTestVerifyTargetEndpointAddress
     *
     * @assertion_ids: WS4EE:SPEC:37; WS4EE:SPEC:39; WS4EE:SPEC:41; WS4EE:SPEC:42; WS4EE:SPEC:43; WS4EE:SPEC:44;
     * WS4EE:SPEC:51; WS4EE:SPEC:109; WS4EE:SPEC:145; WS4EE:SPEC:148; WS4EE:SPEC:149; WS4EE:SPEC:155; WS4EE:SPEC:171;
     * WS4EE:SPEC:184; WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:115; WS4EE:SPEC:213; WS4EE:SPEC:187;
     *
     * @test_Strategy: This is a prebuilt client and prebuilt webservice using EJB endpoint. Tests @WebServiceRef
     * and @WebService annotations. The EJBBean Implementation class is packaged in the ear file. The Remote interface is
     * also packaged in the ear file. The @WebServiceRef uses the name attribute field to access the service ref. The
     * runtime deployment descriptor specifies the endpoint address uri of "WSEjbWebServiceProviderTest/ejb". So verify that
     * "WSEjbWebServiceProviderTest/ejb" is part of the target endpoint address.
     */
    public void WSEjbWebServiceProviderTestVerifyTargetEndpointAddress() throws Fault {
        TestUtil.logMsg("WSEjbWebServiceProviderTestVerifyTargetEndpointAddress");
        try {
            String endpointaddr = JAXWS_Util.getTargetEndpointAddress(port);
            TestUtil.logMsg("Verify that the target endpoint address ends with [WSEjbWebServiceProviderTest/ejb]");
            if (endpointaddr.endsWith("WSEjbWebServiceProviderTest/ejb"))
                TestUtil.logMsg("WSEjbWebServiceProviderTestVerifyTargetEndpointAddress passed");
            else
                throw new RuntimeException("Target Endpoint Address is incorrect: " + endpointaddr);
        } catch (Throwable t) {
            TestUtil.printStackTrace(t);
            throw new Fault("WSEjbWebServiceProviderTestVerifyTargetEndpointAddress failed");
        }
        return;
    }
}
