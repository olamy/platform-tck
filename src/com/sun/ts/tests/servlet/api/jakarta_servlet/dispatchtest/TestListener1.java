/*
 * Copyright (c) 2012, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.servlet.api.jakarta_servlet.dispatchtest;

import jakarta.servlet.AsyncEvent;
import jakarta.servlet.AsyncListener;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;

public final class TestListener1 implements AsyncListener {

    public TestListener1() throws IOException {
    }

    public void onError(AsyncEvent event) throws IOException {
        event.getSuppliedResponse().getWriter().println("in onError method of TestListener1");
        event.getSuppliedResponse().getWriter().println(event.getThrowable().getMessage());
        event.getSuppliedResponse()
                .getWriter()
                .println(event.getSuppliedRequest().getAttribute(RequestDispatcher.ERROR_EXCEPTION));
    }

    public void onStartAsync(AsyncEvent event) throws IOException {
        event.getSuppliedResponse().getWriter().println("in onStartAsync method of TestListener1");
    }

    public void onComplete(AsyncEvent event) throws IOException {
        throw new IOException("in onComplete method of TestListener1");
    }

    public void onTimeout(AsyncEvent event) throws IOException {
        event.getSuppliedResponse().getWriter().println("in onTimeout method of TestListener1");
    }
}
