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

package com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R2744;

import jakarta.jws.WebService;

@WebService(portName = "W2JRLR2744TestPort", serviceName = "W2JRLR2744TestService", targetNamespace = "http://w2jrlr2744testservice.org/W2JRLR2744TestService.wsdl", wsdlLocation = "WEB-INF/wsdl/W2JRLR2744TestService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R2744.W2JRLR2744Test")
public class W2JRLR2744TestImpl implements W2JRLR2744Test {
    public NonNullString echoString(NonNullString str) {
        return str;
    }
}
