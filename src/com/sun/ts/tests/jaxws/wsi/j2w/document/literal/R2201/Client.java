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

package com.sun.ts.tests.jaxws.wsi.j2w.document.literal.R2201;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxws.sharedclients.ClientFactory;
import com.sun.ts.tests.jaxws.sharedclients.SOAPClient;
import com.sun.ts.tests.jaxws.sharedclients.doclitclient.*;
import com.sun.ts.tests.jaxws.wsi.constants.DescriptionConstants;
import com.sun.ts.tests.jaxws.wsi.constants.SOAPConstants;
import com.sun.ts.tests.jaxws.wsi.utils.DescriptionUtils;
import java.util.Properties;
import java.util.StringTokenizer;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Client extends ServiceEETest implements DescriptionConstants, SOAPConstants {
    /**
     * The client.
     */
    private SOAPClient client;

    static J2WDLShared service = null;

    /**
     * Test entry point.
     *
     * @param args the command-line arguments.
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
     * @testName: testPartAttributeContents
     *
     * @assertion_ids: WSI:SPEC:R2201
     *
     * @test_Strategy: Retrieve the WSDL, generated by the Java-to-WSDL tool, and examine all binding elements' operation
     * elements' soap:body elements, ensuring that no more than one part is listed in each part attribute - if present.
     *
     * @throws Fault
     */
    public void testPartAttributeContents() throws Fault {
        Document document = client.getDocument();
        Element[] bindings = DescriptionUtils.getBindings(document);
        for (int i = 0; i < bindings.length; i++) {
            verifyBinding(bindings[i]);
        }
    }

    protected void verifyBinding(Element binding) throws Fault {
        Element[] operations = DescriptionUtils.getChildElements(binding, WSDL_NAMESPACE_URI, WSDL_OPERATION_LOCAL_NAME);
        for (int i = 0; i < operations.length; i++) {
            verifyOperation(operations[i]);
        }
    }

    protected void verifyOperation(Element operation) throws Fault {
        NodeList list = operation.getElementsByTagNameNS(SOAP_NAMESPACE_URI, SOAP_BODY_LOCAL_NAME);
        for (int i = 0; i < list.getLength(); i++) {
            verifySOAPBody((Element) list.item(i));
        }
    }

    protected void verifySOAPBody(Element body) throws Fault {
        Attr attr = body.getAttributeNode(SOAP_PARTS_ATTR);
        if (attr == null) {
            return;
        }
        String parts = attr.getValue();
        StringTokenizer tokenizer = new StringTokenizer(parts, " ");
        int count = tokenizer.countTokens();
        if (count > 1) {
            throw new Fault(
                    "soap:body element encountered with " + count + " parts listed in its part attribute (BP-R2201)");
        }
    }
}
