/*
 * Copyright (c) 2010, 2018, 2020 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.xmloverride.ejbref;

import com.sun.ts.tests.ejb30.common.lite.NumberEnum;
import com.sun.ts.tests.ejb30.common.lite.NumberIF;
import jakarta.ejb.Stateless;

@Stateless
public class XmlOverride2Bean implements NumberIF {

    /*
     * (non-Javadoc)
     *
     * @see com.sun.ts.tests.ejb30.common.lite.NumberIF#add(int)
     */
    public int add(int toAdd) {
        return 2;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sun.ts.tests.ejb30.common.lite.NumberIF#add(com.sun.ts.tests.ejb30. common.lite.NumberIF)
     */
    public NumberIF add(NumberIF toAdd) {
        return NumberEnum.TWO;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sun.ts.tests.ejb30.common.lite.NumberIF#getNumber()
     */
    public int getNumber() {
        return 2; // different from XmlOverrideBean
    }
}
