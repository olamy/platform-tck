package com.sun.ts.tests.servlet.common.util;

import com.sun.ts.tests.servlet.api.jakarta_servlet.scinitializer.getfilterregistration.URLClient;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

public class ResourcesUtils {

    /**
     *
     * @param pathInJar path to create the entry within the temporary jar
     * @param url content on the jar entry
     * @return
     * @throws Exception
     */
    public static Path createTempJarWihOneEntry(String pathInJar, URL url) throws Exception {
        Path tmpJar = Files.createTempFile("tmp",".jar");
        tmpJar.toFile().deleteOnExit();
        try (JarOutputStream jarOutputStream = new JarOutputStream(Files.newOutputStream(tmpJar));
                InputStream inputStream = url.openStream()) {
            JarEntry entry = new JarEntry(pathInJar);
            jarOutputStream.putNextEntry(entry);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while((bytesRead = inputStream.read(buffer)) != -1) {
                jarOutputStream.write(buffer, 0, bytesRead);
            }
        }
        return tmpJar;
    }

}
