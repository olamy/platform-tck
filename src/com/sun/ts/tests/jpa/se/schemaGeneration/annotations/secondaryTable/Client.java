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

package com.sun.ts.tests.jpa.se.schemaGeneration.annotations.secondaryTable;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Client extends PMClientBase {

    private static final long serialVersionUID = 22L;

    String schemaGenerationDir = null;

    public Client() {
    }

    public static void main(String[] args) {
        Client theTests = new Client();
        Status s = theTests.run(args, System.out, System.err);
        s.exit();
    }

    public void setup(String[] args, Properties p) throws Fault {
        TestUtil.logTrace("setup");
        try {
            super.setup(args, p);

            schemaGenerationDir = System.getProperty("user.dir");
            if (!schemaGenerationDir.endsWith(File.separator)) {
                schemaGenerationDir += File.separator;
            }
            schemaGenerationDir += "schemaGeneration";
            TestUtil.logMsg("schemaGenerationDir=" + this.schemaGenerationDir);

            File f = new File(schemaGenerationDir);
            TestUtil.logMsg("Delete existing directory ");
            deleteItem(f);
            TestUtil.logMsg("Create new directory ");
            if (!f.mkdir()) {
                String msg = "Could not mkdir:" + f.getAbsolutePath();
                throw new Fault(msg);
            }
            removeTestData();
        } catch (Exception e) {
            TestUtil.logErr("Exception: ", e);
            throw new Fault("Setup failed:", e);
        }
    }

    /*
     * @testName: secondaryTableTest
     *
     * @assertion_ids: PERSISTENCE:SPEC:2118.16; PERSISTENCE:SPEC:2118.24;
     *
     * @test_Strategy: Test the @SecondaryTable annotation
     */
    public void secondaryTableTest() throws Fault {
        boolean pass1a = false;
        boolean pass1b = false;
        boolean pass1c = false;
        boolean pass2a = false;
        boolean pass2b = false;
        boolean pass2c = false;
        boolean pass3 = false;
        boolean pass4 = false;

        TestUtil.logMsg("Create the script(s)");
        final String CREATEFILENAME = schemaGenerationDir + File.separator + "create_" + this.sTestCase + ".sql";
        final String DROPFILENAME = schemaGenerationDir + File.separator + "drop_" + this.sTestCase + ".sql";

        File f1 = new File(CREATEFILENAME);
        TestUtil.logTrace("Deleting previous create script");
        deleteItem(f1);
        File f2 = new File(DROPFILENAME);
        TestUtil.logTrace("Deleting previous drop script");
        deleteItem(f2);

        Properties props = getPersistenceUnitProperties();
        props.put("jakarta.persistence.schema-generation.database.action", "none");
        props.put("jakarta.persistence.schema-generation.scripts.action", "drop-and-create");
        props.put("jakarta.persistence.schema-generation.create-database-schemas", "false");
        props.put("jakarta.persistence.schema-generation.scripts.create-target", convertToURI(CREATEFILENAME));
        props.put("jakarta.persistence.schema-generation.scripts.drop-target", convertToURI(DROPFILENAME));

        displayProperties(props);

        TestUtil.logMsg("Executing Persistence.createEntityManagerFactory(...)");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(getPersistenceUnitName(), props);
        emf.close();
        emf = null;

        TestUtil.logMsg("Check script(s) content");

        List<String> expected = new ArrayList<String>();
        expected.add("CREATE TABLE SCHEMAGENSIMPLE");
        expected.add("PRODUCT_TYPE");
        expected.add("SIMPLEID");
        expected.add("PRIMARY KEY (SIMPLEID)");
        pass1a = findDataInFile(f1, expected);
        expected.clear();
        expected.add("CREATE TABLE SCHEMAGENSIMPLE_SECOND");
        expected.add("SECONDARY_ID");
        expected.add("PRIMARY KEY (SECONDARY_ID)");
        pass1b = findDataInFile(f1, expected);

        expected.clear();
        expected.add("ALTER TABLE SCHEMAGENSIMPLE_SECOND ADD");
        expected.add("CONSTRAINT MYCONSTRAINT FOREIGN KEY (SECONDARY_ID) REFERENCES SCHEMAGENSIMPLE (SIMPLEID)");
        pass1c = findDataInFile(f1, expected);

        /*
         * CREATE TABLE SCHEMAGENSIMPLE (SIMPLEID INTEGER NOT NULL, PRODUCT_TYPE VARCHAR(31), PRIMARY KEY (SIMPLEID)) CREATE
         * TABLE SCHEMAGENSIMPLE_SECOND (SECONDARY_ID INTEGER NOT NULL, PRIMARY KEY (SECONDARY_ID)) ALTER TABLE
         * SCHEMAGENSIMPLE_SECOND ADD CONSTRAINT MYCONSTRAINT FOREIGN KEY (SECONDARY_ID) REFERENCES SCHEMAGENSIMPLE (SIMPLEID)
         */

        pass2a = findDataInFile(f2, "ALTER TABLE SCHEMAGENSIMPLE_SECOND DROP");
        pass2b = findDataInFile(f2, "DROP TABLE SCHEMAGENSIMPLE");
        pass2c = findDataInFile(f2, "DROP TABLE SCHEMAGENSIMPLE_SECOND");

        TestUtil.logTrace("Execute the create script");
        props = getPersistenceUnitProperties();

        props.put("jakarta.persistence.schema-generation.database.action", "create");
        props.put("jakarta.persistence.schema-generation.scripts.action", "none");
        props.put("jakarta.persistence.schema-generation.create-database-schemas", "true");
        props.put("jakarta.persistence.schema-generation.create-script-source", convertToURI(CREATEFILENAME));
        displayProperties(props);

        TestUtil.logMsg("Executing Persistence.generateSchema(...)");
        Persistence.generateSchema(getPersistenceUnitName(), props);

        clearEMAndEMF();
        try {
            TestUtil.logMsg("Persist some data");
            getEntityTransaction(true).begin();
            Simple s = new Simple(1);
            getEntityManager().persist(s);
            getEntityTransaction().commit();
            clearCache();
            Simple s2 = getEntityManager().find(Simple.class, 1);
            if (s.equals(s2)) {
                TestUtil.logTrace("Received expected result:" + s.toString());
                pass3 = true;
            } else {
                TestUtil.logErr("Expected:" + s.toString());
                TestUtil.logErr("Actual:" + s2.toString());
            }
        } catch (Throwable t) {
            TestUtil.logErr("Received unexpected exception", t);
        }
        clearEMAndEMF();

        TestUtil.logTrace("Execute the drop script");
        props = getPersistenceUnitProperties();
        props.put("jakarta.persistence.schema-generation.database.action", "drop");
        props.put("jakarta.persistence.schema-generation.scripts.action", "none");
        props.put("jakarta.persistence.schema-generation.drop-script-source", convertToURI(DROPFILENAME));
        displayProperties(props);

        TestUtil.logMsg("Executing Persistence.generateSchema(...)");
        Persistence.generateSchema(getPersistenceUnitName(), props);
        clearEMAndEMF();

        TestUtil.logMsg("Try to persist an entity, it should fail");
        try {
            getEntityTransaction(true).begin();
            Simple s3 = new Simple(2);
            getEntityManager().persist(s3);
            getEntityManager().flush();
            getEntityTransaction().commit();
            TestUtil.logErr("An exception should have been thrown if drop had occurred successfully");
        } catch (Exception ex) {
            TestUtil.logTrace("Receive expected exception");
            pass4 = true;
        }
        TestUtil.logTrace("pass1a:" + pass1a);
        TestUtil.logTrace("pass1b:" + pass1b);
        TestUtil.logTrace("pass1c:" + pass1c);
        TestUtil.logTrace("pass2a:" + pass2a);
        TestUtil.logTrace("pass2b:" + pass2b);
        TestUtil.logTrace("pass2c:" + pass2c);
        TestUtil.logTrace("pass3:" + pass3);
        TestUtil.logTrace("pass4:" + pass4);
        if (!pass1a || !pass1b || !pass1c || !pass2a || !pass2b || !pass2c || !pass3 || !pass4) {
            throw new Fault("secondaryTableTest failed");
        }
    }

    public void cleanup() throws Fault {
        TestUtil.logTrace("cleanup");
        removeTestData();
        TestUtil.logTrace("cleanup complete, calling super.cleanup");
        super.cleanup();
    }

    private void removeTestData() {
        TestUtil.logTrace("removeTestData");
        if (getEntityTransaction().isActive()) {
            getEntityTransaction().rollback();
        }
        try {
            getEntityTransaction().begin();
            TestUtil.logMsg("Try to drop table SCHEMAGENSIMPLE");
            getEntityManager().createNativeQuery("DROP TABLE SCHEMAGENSIMPLE").executeUpdate();
            getEntityTransaction().commit();
        } catch (Throwable t) {
            TestUtil.logMsg(
                    "AN EXCEPTION WAS THROWN DURING DROP TABLE SCHEMAGENSIMPLE, IT MAY OR MAY NOT BE A PROBLEM, "
                            + t.getMessage());
        } finally {
            try {
                if (getEntityTransaction().isActive()) {
                    getEntityTransaction().rollback();
                }
                clearEntityTransaction();

                // ensure that we close the EM and EMF before proceeding.
                clearEMAndEMF();
            } catch (Exception re) {
                TestUtil.logErr("Unexpected Exception in removeTestData:", re);
            }
        }
    }
}
