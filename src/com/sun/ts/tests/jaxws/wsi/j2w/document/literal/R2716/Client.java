/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxws.wsi.j2w.document.literal.R2716;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jaxws.sharedclients.ClientFactory;
import com.sun.ts.tests.jaxws.sharedclients.SOAPClient;
import com.sun.ts.tests.jaxws.sharedclients.doclitclient.*;
import com.sun.ts.tests.jaxws.wsi.j2w.NamespaceAttributeVerifier;
import java.util.Properties;
import org.w3c.dom.Document;

public class Client extends ServiceEETest {
    /**
     * The client.
     */
    private SOAPClient client;

    static J2WDLShared service = null;

    /**
     * Test entry point.
     *
     * @param args
     *          the command-line arguments.
     */
    public static void main(String[] args) {
        Client test = new Client();
        Status status = test.run(args, System.out, System.err);
        status.exit();
    }

    /**
     * @class.testArgs: -ap jaxws-url-props.dat
     * @class.setup_props: webServerHost; webServerPort; platform.mode;
     *
     * @param args
     * @param properties
     *
     * @throws Fault
     */
    public void setup(String[] args, Properties properties) throws Fault {
        client = ClientFactory.getClient(J2WDLSharedClient.class, properties, this, service);
        logMsg("setup ok");
    }

    public void cleanup() {
        logMsg("cleanup");
    }

    /**
     * @testName: testSOAPElementNamespace
     *
     * @assertion_ids: WSI:SPEC:R2716
     *
     * @test_Strategy: Retrieve the WSDL, generated by the Java-to-WSDL tool, and
     *                 examine the document-literal wsdl:binding elements to
     *                 ensure that their input- and output elements' soap:body,
     *                 soap:header, soap:headerfault and soap:fault elements do
     *                 not have a namespace attribute defined.
     *
     * @throws Fault
     */
    public void testSOAPElementNamespace() throws Fault {
        Document document = client.getDocument();
        NamespaceAttributeVerifier verifier = new NamespaceAttributeVerifier(document, 2716);
        verifier.verify();
    }
}
