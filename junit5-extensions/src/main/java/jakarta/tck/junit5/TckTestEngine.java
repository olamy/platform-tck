package jakarta.tck.junit5;

import org.junit.jupiter.engine.JupiterTestEngine;
import org.junit.jupiter.engine.config.CachingJupiterConfiguration;
import org.junit.jupiter.engine.config.DefaultJupiterConfiguration;
import org.junit.jupiter.engine.config.JupiterConfiguration;
import org.junit.jupiter.engine.descriptor.JupiterEngineDescriptor;
import org.junit.jupiter.engine.discovery.predicates.IsTestClassWithTests;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.EngineExecutionListener;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.Filter;
import org.junit.platform.engine.FilterResult;
import org.junit.platform.engine.SelectorResolutionResult;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestEngine;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.discovery.MethodSelector;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.descriptor.ClassSource;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;
import org.junit.platform.engine.support.descriptor.MethodSource;
import org.junit.platform.engine.support.discovery.EngineDiscoveryRequestResolver;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryListener;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.LauncherSession;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherConfig;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.junit.platform.reporting.legacy.xml.LegacyXmlReportGeneratingListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class TckTestEngine implements TestEngine {

    private static final boolean NO_FILTERING_TESTS = Boolean.getBoolean("jakarta.tck.notfilter.tests");

    private List<String> testClassesAndMethods = new ArrayList<>();

    private JupiterEngineDescriptor engine;

    private JupiterTestEngine jupiterTestEngine = new JupiterTestEngine();

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
        if (engine != null) {
            return engine;
        }
        JupiterConfiguration configuration = new CachingJupiterConfiguration(
                new DefaultJupiterConfiguration(request.getConfigurationParameters()));
        engine = new JupiterEngineDescriptor(uniqueId, configuration);
        testClassesAndMethods.forEach(
                s -> {
                    UniqueId uid = uniqueId.append("test", s);
                    MethodSource methodSource = MethodSource.from(s.substring(0, s.indexOf('#')), s.substring(s.indexOf('#')+1), "");
                    TCKDescriptor tckDescriptor = new TCKDescriptor(uid, s, methodSource);
                    engine.addChild(tckDescriptor);
                }
        );
        System.out.println("---------------------");
        System.out.println("TCK Tests:" + testClassesAndMethods.size());
        System.out.println("---------------------");


//        try (LauncherSession session = LauncherFactory.openSession()) {
//            Launcher launcher = session.getLauncher();
//
//            LauncherDiscoveryRequest launcherDiscoveryRequest = LauncherDiscoveryRequestBuilder.request()
//                    .selectors(
//                            testClassesAndMethods.stream().map(s -> DiscoverySelectors.selectMethod(s)).collect(Collectors.toUnmodifiableList())
//                    ).listeners(new MyDiscoveryListener(engine, uniqueId))
//                    .build();
//            TestPlan testPlan = launcher.discover(launcherDiscoveryRequest);
//            testPlan.getConfigurationParameters();
//        }

        return engine;

    }

    private static class MyDiscoveryListener implements LauncherDiscoveryListener {

        private final JupiterEngineDescriptor jupiterEngineDescriptor;

        private final UniqueId uniqueId;

        public MyDiscoveryListener(JupiterEngineDescriptor jupiterEngineDescriptor, UniqueId uniqueId) {
            this.jupiterEngineDescriptor = jupiterEngineDescriptor;
            this.uniqueId = uniqueId;
        }

        @Override
        public void selectorProcessed(UniqueId engineId, DiscoverySelector selector, SelectorResolutionResult result) {
            if (result.getStatus() == SelectorResolutionResult.Status.RESOLVED && selector instanceof MethodSelector) {
                MethodSelector methodSelector = ((MethodSelector)selector);
                MethodSource methodSource = MethodSource.from(methodSelector.getClassName(),
                        methodSelector.getMethodName(),
                        methodSelector.getMethodParameterTypes());
                UniqueId uid = uniqueId.append("test", methodSource.toString());
                TCKDescriptor tckDescriptor = new TCKDescriptor(uid, methodSource.toString(), methodSource);
                jupiterEngineDescriptor.addChild(tckDescriptor);
            }

        }
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
        EngineExecutionListener engineExecutionListener = executionRequest.getEngineExecutionListener();

        //SummaryGeneratingListener listener = new SummaryGeneratingListener();

        Path reportDir = Paths.get("target/surefire-reports");
        if (!Files.exists(reportDir)) {
            try {
                Files.createDirectories(reportDir);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        PrintWriter printWriter = new PrintWriter(System.out, true);
        LegacyXmlReportGeneratingListener listener = new LegacyXmlReportGeneratingListener(reportDir, printWriter);

        try (LauncherSession session = LauncherFactory.openSession()) {
            Launcher launcher = session.getLauncher();
            launcher.registerTestExecutionListeners(listener);

            LauncherDiscoveryRequest launcherDiscoveryRequest = LauncherDiscoveryRequestBuilder.request()
                    .selectors(
                            testClassesAndMethods.stream().map(s -> DiscoverySelectors.selectMethod(s)).collect(Collectors.toUnmodifiableList())
                    )
                    .build();
            TestPlan testPlan = launcher.discover(launcherDiscoveryRequest);

            //TestPlan testPlan = TestPlan.from((Collection<TestDescriptor>) engine.getChildren(), executionRequest.getConfigurationParameters());
            launcher.execute(testPlan, new MyListener());

        }

//        TestExecutionSummary summary = listener.getSummary();
//        System.out.println(summary);
    }

    private static class MyListener implements TestExecutionListener {
        @Override
        public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
            TestExecutionListener.super.executionFinished(testIdentifier, testExecutionResult);
        }
    }


}
