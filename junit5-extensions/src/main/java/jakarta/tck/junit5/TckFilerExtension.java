package jakarta.tck.junit5;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

public class TckFilerExtension implements InvocationInterceptor {

    private static final boolean NO_FILTERING_TESTS = Boolean.getBoolean("jakarta.tck.notfilter.tests");

    private List<String> testClassesAndMethods = new ArrayList<>();

    public TckFilerExtension() {
        try {
            Enumeration<URL> urls = getClass().getClassLoader().getResources("META-INF/tck-tests.txt");
            while (urls.hasMoreElements()) {
                URL file = urls.nextElement();
                try (InputStream is = file.openStream();
                     BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                    List<String> tests = br.lines().collect(Collectors.toList());
                    testClassesAndMethods.addAll(tests);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    @Override
    public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
//        if (invocationContext.getExecutable().getAnnotation(Test.class)!=null && !NO_FILTERING_TESTS) {
//            String fqcnMethodName = invocationContext.getTargetClass().getName() + "#" + invocationContext.getExecutable().getName();
//            if (!testClassesAndMethods.contains(fqcnMethodName)){
//                invocation.skip();
//            }
//        } else {
//            InvocationInterceptor.super.interceptTestMethod(invocation, invocationContext, extensionContext);
//        }
        InvocationInterceptor.super.interceptTestMethod(invocation, invocationContext, extensionContext);
    }
}
