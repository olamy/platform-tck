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

package com.sun.ts.tests.jpa.se.schemaGeneration.annotations.orderColumn;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "SCHEMAGENDEPT")
public class Department implements java.io.Serializable {

    // Instance variables
    private int deptId;

    private List<Employee> employees;

    public Department() {
    }

    public Department(int id) {
        this.deptId = id;
    }

    @Id
    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int id) {
        this.deptId = id;
    }

    @OneToMany(mappedBy = "department")
    @OrderColumn(name = "THEORDERCOLUMN")
    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(this.getClass().getSimpleName() + "[");
        result.append("id: " + getDeptId());
        result.append("]");
        return result.toString();
    }

    public boolean equals(Object o) {
        Department other;
        boolean result = false;

        if (!(o instanceof Department)) {
            return result;
        }
        other = (Department) o;

        if (this.getDeptId() == other.getDeptId()) {
            result = true;
        }

        return result;
    }

    public int hashCode() {
        int myHash;

        myHash = this.getDeptId();

        return myHash;
    }
}
