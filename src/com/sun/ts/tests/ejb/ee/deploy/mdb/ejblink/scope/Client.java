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

package com.sun.ts.tests.ejb.ee.deploy.mdb.ejblink.scope;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import jakarta.jms.Queue;
import java.util.Properties;

public class Client extends com.sun.ts.tests.jms.commonee.Client {

    private Queue mdbQ;

    public static void main(String[] args) {
        Client theTests = new Client();
        Status s = theTests.run(args, System.out, System.err);
        s.exit();
    }

    /**
     * @class.setup_props: jms_timeout; user; password;
     *
     */
    public void setup(String[] args, Properties props) throws Fault {

        try {
            this.props = props;
            super.setup(args, props);

            mdbQ = (Queue) context.lookup("java:comp/env/jms/MDBTest");
        } catch (Exception e) {
            TestUtil.logErr("[Client] Setup failed!", e);
            throw new Fault("Setup Failed!", e);
        }
    }

    /**
     * @testName: testEjblinkScope
     *
     * @assertion_ids: EJB:SPEC:766
     *
     * @test_Strategy: A MDB bean references two other beans. One in the same JAR file, the other in a distinct JAR file.
     * Both referenced bean use the same ejb-name in their respective JAR file, and their are identified by a String
     * environment entry ('myName').
     *
     * The ejb-link for the external bean is in the form '../external.jar#BeanName'.
     *
     * Check that we can deploy the application, that the referencing bean can lookup the two other ones. Check that
     * referenced beans's identities (as reported by the String env. entry) match the ones specified in the DD.
     */
    public void testEjblinkScope() throws Fault {

        String testCase = "testEjblinkScope";
        int testNum = 10;

        try {
            qSender = session.createSender(mdbQ);
            createTestMessage(testCase, testNum);
            qSender.send(msg);

            if (!checkOnResponse(testCase)) {
                TestUtil.logErr("[Client] " + testCase + " failed!");
                throw new Exception(testCase + " failed!");
            }
        } catch (Exception e) {
            TestUtil.logErr("[Client] " + testCase + " failed!", e);
            throw new Fault(testCase + " failed!", e);
        }
    }
}
