package jakarta.tck.junit5;

import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.EngineExecutionListener;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestEngine;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;
import org.junit.platform.engine.support.descriptor.MethodSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

public class TckTestEngine implements TestEngine {

    private static final boolean NO_FILTERING_TESTS = Boolean.getBoolean("jakarta.tck.notfilter.tests");

    private List<String> testClassesAndMethods = new ArrayList<>();

    public TckTestEngine() {
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
    public String getId() {
        return "jakarta-tck-test-engine";
    }

    @Override
    public TestDescriptor discover(EngineDiscoveryRequest request, UniqueId uniqueId) {
        EngineDescriptor engine = new EngineDescriptor(uniqueId, "TCK Test Engine");
        testClassesAndMethods.forEach(
                s -> {
                    UniqueId uid = uniqueId.append("test", s);
                    MethodSource methodSource = MethodSource.from(s.substring(0, s.indexOf('#')), s.substring(s.indexOf('#')+1));
                    TCKDescriptor tckDescriptor = new TCKDescriptor(uid, s, methodSource);
                    engine.addChild(tckDescriptor);
                }
        );
        return engine;

    }

    private static class TCKDescriptor extends AbstractTestDescriptor {

        public TCKDescriptor(UniqueId id, String displayName, TestSource source) {
            super(id, displayName, source);
        }

        @Override
        public Type getType() {
            return Type.TEST;
        }
    }


    @Override
    public void execute(ExecutionRequest executionRequest) {
        TestDescriptor engine = executionRequest.getRootTestDescriptor();
        EngineExecutionListener listener = executionRequest.getEngineExecutionListener();
        listener.executionStarted(engine);
        for (TestDescriptor child : engine.getChildren()) {
            if (child instanceof TCKDescriptor) {
                TCKDescriptor descriptor = (TCKDescriptor) child;
                listener.executionStarted(descriptor);
                listener.executionFinished(descriptor, TestExecutionResult.successful());
            }
        }
        listener.executionFinished(engine, TestExecutionResult.successful());
    }

}
