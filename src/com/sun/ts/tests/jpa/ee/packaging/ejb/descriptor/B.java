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

package com.sun.ts.tests.jpa.ee.packaging.ejb.descriptor;

public class B implements java.io.Serializable {

    // ===========================================================
    // instance variables

    protected String id;

    protected String name;

    protected int value;

    protected A a;

    // ===========================================================
    // constructors

    public B() {}

    public B(String id, String name, int value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public B(String id, String name, int value, A a) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.a = a;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    // ===========================================================
    // relationship fields

    public A getA() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }
}
