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

package com.sun.ts.tests.jsp.api.jakarta_servlet.jsp.tagext.bodytagsupport;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

public class GetPreviousOutTestTag extends BodyTagSupport {

    /**
     * Default constructor.
     */
    public GetPreviousOutTestTag() {
        super();
    }

    /**
     * Validates getPreviousOut().
     *
     * @return Tag.EVAL_PAGE
     * @throws JspException - if an error occurs
     */
    public int doEndTag() throws JspException {
        try {
            JspWriter out = this.getPreviousOut();

            // clear the buffer from the page
            out.clear();
            out.println("Test PASSED");
        } catch (IOException ioe) {
            throw new JspException("Unexpected Exception!", ioe);
        }
        return EVAL_PAGE;
    }
}
