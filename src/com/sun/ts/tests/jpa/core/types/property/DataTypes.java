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

package com.sun.ts.tests.jpa.core.types.property;

import com.sun.ts.tests.jpa.core.types.common.Grade;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "DATATYPES")
public class DataTypes implements java.io.Serializable {

    private Integer id;

    private Character characterData;

    private Boolean booleanData;

    private Short shortData;

    private Integer integerData;

    private Long longData;

    private Double doubleData;

    private Float floatData;

    private Grade enumData;

    private Byte[] byteArrayData;

    private Character[] charArrayData;

    public DataTypes() {}

    public DataTypes(
            Integer id,
            Character characterData,
            Boolean booleanData,
            Short shortData,
            Integer integerData,
            Long longData,
            Double doubleData,
            Float floatData,
            Character[] charArrayData,
            Byte[] byteArrayData) {
        this.id = id;
        this.characterData = characterData;
        this.booleanData = booleanData;
        this.shortData = shortData;
        this.integerData = integerData;
        this.longData = longData;
        this.doubleData = doubleData;
        this.floatData = floatData;
        this.byteArrayData = byteArrayData;
        this.charArrayData = charArrayData;
    }

    @Id
    @Column(name = "ID")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "CHARDATA")
    public Character getCharacterData() {
        return characterData;
    }

    public void setCharacterData(Character characterData) {
        this.characterData = characterData;
    }

    @Column(name = "BOOLEANDATA")
    public Boolean getBooleanData() {
        return booleanData;
    }

    public void setBooleanData(Boolean booleanData) {
        this.booleanData = booleanData;
    }

    @Column(name = "SHORTDATA")
    public Short getShortData() {
        return shortData;
    }

    public void setShortData(Short shortData) {
        this.shortData = shortData;
    }

    @Column(name = "INTDATA")
    public Integer getIntegerData() {
        return integerData;
    }

    public void setIntegerData(Integer integerData) {
        this.integerData = integerData;
    }

    @Column(name = "LONGDATA")
    public Long getLongData() {
        return longData;
    }

    public void setLongData(Long longData) {
        this.longData = longData;
    }

    @Column(name = "DBLDATA")
    public Double getDoubleData() {
        return doubleData;
    }

    public void setDoubleData(Double doubleData) {
        this.doubleData = doubleData;
    }

    @Column(name = "FLOATDATA")
    public Float getFloatData() {
        return floatData;
    }

    public void setFloatData(Float floatData) {
        this.floatData = floatData;
    }

    @Basic
    @Column(name = "ENUMODATA")
    @Enumerated(EnumType.ORDINAL)
    public Grade getEnumData() {
        return enumData;
    }

    public void setEnumData(Grade grade) {
        this.enumData = grade;
    }

    @Column(name = "CHARARRAYDATA")
    public Character[] getCharArrayData() {
        return charArrayData;
    }

    public void setCharArrayData(Character[] charArrayData) {
        this.charArrayData = charArrayData;
    }

    @Lob
    @Column(name = "BYTEARRAYDATA")
    public Byte[] getByteArrayData() {
        return byteArrayData;
    }

    public void setByteArrayData(Byte[] byteArrayData) {
        this.byteArrayData = byteArrayData;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(this.getClass().getSimpleName() + "[");
        result.append("id: " + getId());
        result.append(", boolean: " + getBooleanData());
        result.append(", char: " + getCharacterData());
        result.append(", short: " + getShortData());
        result.append(", integer: " + getIntegerData());
        result.append(", long: " + getLongData());
        result.append(", double: " + getDoubleData());
        result.append(", float: " + getFloatData());

        if (getCharArrayData() != null && getCharArrayData().length > 0) {
            StringBuilder sb = new StringBuilder(", CharData: [");
            for (int i = 0; i < charArrayData.length; i++) {
                sb.append(charArrayData[i]);
                if (i < charArrayData.length - 1) {
                    sb.append(", ");
                }
            }
            sb.append("],");
            result.append(sb.toString());
        } else {
            result.append(", CharData: null");
        }
        if (getByteArrayData() != null && getByteArrayData().length > 0) {
            StringBuilder sb = new StringBuilder(", ByteData: [");
            for (int i = 0; i < byteArrayData.length; i++) {
                sb.append(byteArrayData[i]);
                if (i < byteArrayData.length - 1) {
                    sb.append(", ");
                }
            }
            sb.append("],");
            result.append(sb.toString());
        } else {
            result.append(", ByteData: null");
        }
        result.append("]");
        return result.toString();
    }
}
