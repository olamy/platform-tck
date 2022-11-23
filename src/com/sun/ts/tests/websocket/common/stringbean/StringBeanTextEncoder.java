/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates and others.
 * All rights reserved.
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

package com.sun.ts.tests.websocket.common.stringbean;

import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder.Text;
import jakarta.websocket.EndpointConfig;

public class StringBeanTextEncoder implements Text<StringBean> {

    @Override
    public void init(EndpointConfig config) {}

    @Override
    public String encode(StringBean bean) throws EncodeException {
        return bean.get();
    }

    @Override
    public void destroy() {}
}
