package com.sun.ts.tests.servlet.common.servlets;

import com.sun.ts.tests.servlet.common.response.HttpResponseTestServlet;
import com.sun.ts.tests.servlet.common.response.ResponseTestServlet;
import com.sun.ts.tests.servlet.common.response.ResponseTests;
import com.sun.ts.tests.servlet.common.util.Data;
import com.sun.ts.tests.servlet.common.util.ServletTestUtil;
import com.sun.ts.tests.servlet.common.util.StaticLog;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommonServlets {

    private static final CommonServlets INSTANCE = new CommonServlets();

    private final JavaArchive[] javaArchives;

    private CommonServlets() {
        try {
            List<JavaArchive> archives = new ArrayList<>();

            File[] files = Maven.resolver().loadPomFromFile("pom.xml")
                    .resolve("org.slf4j:slf4j-simple")
                    .withTransitivity()
                    .asFile();
            List<JavaArchive> slf4jJars =
                    Arrays.stream(files).map(file -> ShrinkWrap.createFromZipFile(JavaArchive.class, file))
                            .collect(Collectors.toList());

            //archives.addAll(slf4jJars);

            archives.add(ShrinkWrap.create(JavaArchive.class, "common-servlets.jar")
                    .addClasses(GenericCheckTestResultServlet.class, GenericTCKServlet.class, RequestTestServlet.class,
                            HttpCheckTestResultServlet.class, HttpRequestTestServlet.class, RequestTests.class,
                            HttpTCKServlet.class, Data.class, StaticLog.class, ServletTestUtil.class,
                            ResponseTests.class, ResponseTestServlet.class, HttpResponseTestServlet.class));

            javaArchives = archives.toArray(new JavaArchive[0]);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    public static JavaArchive[] getCommonServletsArchive() {
        return INSTANCE.javaArchives;
    }

}
