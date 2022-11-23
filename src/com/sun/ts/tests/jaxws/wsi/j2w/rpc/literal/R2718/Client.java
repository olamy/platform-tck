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

package com.sun.ts.tests.jaxws.wsi.j2w.rpc.literal.R2718;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxws.sharedclients.ClientFactory;
import com.sun.ts.tests.jaxws.sharedclients.SOAPClient;
import com.sun.ts.tests.jaxws.sharedclients.rpclitclient.*;
import com.sun.ts.tests.jaxws.wsi.constants.DescriptionConstants;
import com.sun.ts.tests.jaxws.wsi.utils.DescriptionUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Client extends ServiceEETest implements DescriptionConstants {
    /**
     * The client.
     */
    private SOAPClient client;

    static J2WRLShared service = null;

    /**
     * The document.
     */
    private Document document;

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
        client = ClientFactory.getClient(J2WRLSharedClient.class, properties, this, service);
        logMsg("setup ok");
    }

    public void cleanup() {
        logMsg("cleanup");
    }

    /**
     * @testName: testBindingOperations
     *
     * @assertion_ids: WSI:SPEC:R2718
     *
     * @test_Strategy: Retrieve the WSDL, generated by the Java-to-WSDL tool, and
     *                 examine the wsdl:binding elements, ensuring that the list
     *                 of operations match exactly the list of operations in the
     *                 references portType element(s).
     *
     * @throws Fault
     */
    public void testBindingOperations() throws Fault {
        document = client.getDocument();
        Element[] bindings = DescriptionUtils.getBindings(document);
        for (int i = 0; i < bindings.length; i++) {
            verifyBinding(bindings[i]);
        }
    }

    protected void verifyBinding(Element binding) throws Fault {
        Element portType = getPortType(binding);
        ArrayList bindingNames = getOperationNames(binding);
        ArrayList portTypeNames = getOperationNames(portType);
        Iterator iterator = bindingNames.iterator();
        while (iterator.hasNext()) {
            String name = (String) iterator.next();
            if (!portTypeNames.contains(name)) {
                throw new Fault(
                        "Operation '" + name + "' in wsdl:binding not present in referenced wsdl:portType (BP-R2718)");
            }
            portTypeNames.remove(name);
        }
        int names = portTypeNames.size();
        if (names > 0) {
            throw new Fault(
                    names
                            + " operations listed in referenced wsdl:portType are not present in referencing wsdl:binding (BP-R2718)");
        }
    }

    protected Element getPortType(Element binding) throws Fault {
        String type = binding.getAttribute(WSDL_TYPE_ATTR);
        int index = type.indexOf(':');
        if (index > 0) {
            type = type.substring(index + 1);
        }
        Element[] portTypes = DescriptionUtils.getPortTypes(document);
        for (int i = 0; i < portTypes.length; i++) {
            String name = portTypes[i].getAttribute(WSDL_NAME_ATTR);
            if (type.equals(name)) {
                return portTypes[i];
            }
        }
        throw new Fault("Port type '" + type + "' referenced in wsdl:binding not found (BP-R2718)");
    }

    protected ArrayList getOperationNames(Element element) {
        ArrayList names = new ArrayList();
        Element[] operations =
                DescriptionUtils.getChildElements(element, WSDL_NAMESPACE_URI, WSDL_OPERATION_LOCAL_NAME);
        for (int i = 0; i < operations.length; i++) {
            String name = operations[i].getAttribute(WSDL_NAME_ATTR);
            names.add(name);
        }
        return names;
    }
}
