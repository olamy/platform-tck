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

package com.sun.ts.tests.jpa.core.metamodelapi.managedtype;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MapKey;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Map;

/*
 * Department
 */

@Entity
@Table(name = "DEPARTMENT")
public class Department implements java.io.Serializable {

    // Instance variables
    private int id;

    private String name;

    private Map<String, Employee> lastNameEmployees;

    public Department() {}

    public Department(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // ===========================================================
    // getters and setters for the state fields

    @Id
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // ===========================================================
    // getters and setters for the association fields

    @OneToMany(mappedBy = "department")
    @MapKey(name = "lastName")
    public Map<String, Employee> getLastNameEmployees() {
        return lastNameEmployees;
    }

    public void setLastNameEmployees(Map<String, Employee> lastNameEmployees) {
        this.lastNameEmployees = lastNameEmployees;
    }
}
