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

package com.sun.ts.tests.websocket.ee.jakarta.websocket.clientendpointonmessage;

import com.sun.ts.tests.websocket.common.client.AnnotatedClientEndpoint;
import com.sun.ts.tests.websocket.common.stringbean.StringBean;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanClientEndpoint;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanTextDecoder;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.CloseReason;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import java.io.IOException;

@ClientEndpoint(decoders = { StringBeanTextDecoder.class })
public class WSTextDecoderAndSessionClientEndpoint extends AnnotatedClientEndpoint<StringBean> {

    public WSTextDecoderAndSessionClientEndpoint() {
        super(new StringBeanClientEndpoint());
    }

    @OnMessage
    public void echo(StringBean bean, Session s) throws IOException {
        super.onMessage(bean);
        s.getBasicRemote().sendText(bean.get());
    }

    @OnError
    @Override
    public void onError(Session session, Throwable t) {
        super.onError(session, t);
    }

    @OnClose
    @Override
    public void onClose(Session session, CloseReason closeReason) {
        super.onClose(session, closeReason);
    }

    @OnOpen
    @Override
    public void onOpen(Session session, EndpointConfig config) {
        super.onOpen(session, config);
    }
}
