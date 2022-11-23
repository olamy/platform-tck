/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsonp.provider;

import com.sun.ts.lib.util.TestUtil;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonParser;
import jakarta.json.stream.JsonParserFactory;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Map;

/*
 * MyJsonParserFactory is a Json Test ParserFactory used by the pluggability tests
 * to test the Json SPI layer. This parser tracks that the proper callback
 * methods are invoked within the parser when Json API methods are called.
 */

public class MyJsonParserFactory implements JsonParserFactory {
    private InputStream in = null;

    private Charset charset = null;

    private Reader reader = null;

    private Map<String, ?> config = null;

    private void dumpInstanceVars() {
        TestUtil.logTrace("reader=" + reader);
        TestUtil.logTrace("in=" + in);
        TestUtil.logTrace("charset=" + charset);
        TestUtil.logTrace("config=" + config);
    }

    // call methods
    private static StringBuilder calls = new StringBuilder();

    public static String getCalls() {
        return calls.toString();
    }

    public static void clearCalls() {
        calls.delete(0, calls.length());
    }

    private static void addCalls(String s) {
        calls.append(s);
    }

    public MyJsonParserFactory(Map<String, ?> config) {
        this.config = config;
    }

    public Map<String, ?> getConfigInUse() {
        TestUtil.logTrace("public Map<String, ?> getConfigInUse()");
        addCalls("public Map<String, ?> getConfigInUse()");
        return config;
    }

    public JsonParser createParser(InputStream in) {
        TestUtil.logTrace("public JsonParser createParser(InputStream)");
        addCalls("public JsonParser createParser(InputStream)");
        this.in = in;
        return null;
    }

    public JsonParser createParser(InputStream in, Charset charset) {
        TestUtil.logTrace("public JsonParser createParser(InputStream, Charset)");
        addCalls("public JsonParser createParser(InputStream, Charset)");
        this.in = in;
        this.charset = charset;
        return null;
    }

    public JsonParser createParser(Reader reader) {
        TestUtil.logTrace("public JsonParser createParser(Reader)");
        addCalls("public JsonParser createParser(Reader)");
        this.reader = reader;
        return null;
    }

    public JsonParser createParser(JsonArray jsonArray) {
        TestUtil.logTrace("public JsonParser createParser(JsonArray)");
        addCalls("public JsonParser createParser(JsonArray)");
        return null;
    }

    public JsonParser createParser(JsonObject jsonObject) {
        TestUtil.logTrace("public JsonParser createParser(JsonObject)");
        addCalls("public JsonParser createParser(JsonObject)");
        return null;
    }
}
