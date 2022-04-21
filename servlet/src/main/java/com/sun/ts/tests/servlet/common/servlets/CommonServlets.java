package com.sun.ts.tests.servlet.common.servlets;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

import java.util.Arrays;
import java.util.List;

public class CommonServlets {

    private CommonServlets() {
        // nothing
    }

    public static JavaArchive getCommonServletsArchive() {
        return ShrinkWrap.create(JavaArchive.class, "common-servlets.jar")
                .addClasses(GenericCheckTestResultServlet.class, GenericTCKServlet.class,
                            HttpCheckTestResultServlet.class, HttpRequestTestServlet.class,
                            HttpTCKServlet.class);
    }

}
