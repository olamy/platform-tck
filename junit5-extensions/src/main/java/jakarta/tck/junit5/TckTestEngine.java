package jakarta.tck.junit5;

import org.junit.jupiter.engine.JupiterTestEngine;
import org.junit.jupiter.engine.config.CachingJupiterConfiguration;
import org.junit.jupiter.engine.config.DefaultJupiterConfiguration;
import org.junit.jupiter.engine.config.JupiterConfiguration;
import org.junit.jupiter.engine.descriptor.JupiterEngineDescriptor;
import org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.EngineExecutionListener;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.SelectorResolutionResult;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestEngine;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.discovery.MethodSelector;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.descriptor.MethodSource;
import org.junit.platform.launcher.EngineFilter;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryListener;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.LauncherSession;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.junit.platform.reporting.legacy.xml.LegacyXmlReportGeneratingListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class TckTestEngine implements TestEngine {

    private static final Logger LOGGER = LoggerFactory.getLogger(TckTestEngine.class);

    private static final String ENGINE_ID = "jakarta-tck-test-engine";

    private List<String> testClassesAndMethods = new ArrayList<>();

    private JupiterEngineDescriptor engine;

    private JupiterTestEngine jupiterTestEngine = new JupiterTestEngine();

    private JupiterConfiguration jupiterConfiguration;

    private UniqueId uniqueId;

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
        return ENGINE_ID;
    }

    @Override
    public TestDescriptor discover(EngineDiscoveryRequest request, UniqueId uniqueId) {
        if (engine != null) {
            return engine;
        }
        this.jupiterConfiguration = new CachingJupiterConfiguration(
                new DefaultJupiterConfiguration(request.getConfigurationParameters()));
        this.engine = new JupiterEngineDescriptor(uniqueId, this.jupiterConfiguration);
        this.uniqueId = uniqueId;
        this.testClassesAndMethods.forEach(
                s -> {
                    UniqueId uid = uniqueId.append("test", s);
                    MethodSource methodSource = MethodSource.from(s.substring(0, s.indexOf('#')), s.substring(s.indexOf('#')+1), "");
                    TCKDescriptor tckDescriptor = new TCKDescriptor(uid, s, methodSource);
                    engine.addChild(tckDescriptor);
                }
        );
        LOGGER.info("TCK Tests:" + testClassesAndMethods.size());

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

        // TODO path configurable via system property
        Path reportDir = Paths.get("target/tck-tests-reports");
        if (!Files.exists(reportDir)) {
            try {
                Files.createDirectories(reportDir);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        PrintWriter printWriter = new PrintWriter(System.out, true);
        LegacyXmlReportGeneratingListener legacyXmlReportGeneratingListener = new LegacyXmlReportGeneratingListener(reportDir, printWriter);
        SummaryGeneratingListener summaryGeneratingListener = new SummaryGeneratingListener();

        try (LauncherSession session = LauncherFactory.openSession()) {
            Launcher launcher = session.getLauncher();
            launcher.registerTestExecutionListeners(legacyXmlReportGeneratingListener);

            // here we don't want an infinite loop so ignore it self
            LauncherDiscoveryRequestBuilder builder = LauncherDiscoveryRequestBuilder.request()
                    .filters(EngineFilter.excludeEngines(ENGINE_ID, "junit-vintage"));
            String classesNames = System.getProperty(TckTestEngine.class.getName() + ".classesNames", "");
            if (classesNames.isEmpty()) {
                builder.selectors(testClassesAndMethods.stream()
                        .map(s -> DiscoverySelectors.selectMethod(s)).collect(Collectors.toUnmodifiableList()));
            } else {
                List<String> classesAndOrMethod = Arrays.stream(classesNames.split(";")).collect(Collectors.toList());
                builder.selectors(classesAndOrMethod.stream()
                        .map(string ->
                            string.contains("#")? DiscoverySelectors.selectMethod(string):DiscoverySelectors.selectClass(string)
                        )
                        .collect(Collectors.toUnmodifiableList()));
            }


            LauncherDiscoveryRequest launcherDiscoveryRequest = builder.build();
            TestPlan testPlan = launcher.discover(launcherDiscoveryRequest);

            //TestPlan testPlan = TestPlan.from((Collection<TestDescriptor>) engine.getChildren(), executionRequest.getConfigurationParameters());
            MyListener myListener = new MyListener(executionRequest.getEngineExecutionListener(), this.jupiterConfiguration, this.uniqueId);
            launcher.execute(testPlan, myListener, summaryGeneratingListener);

            TestExecutionSummary summary = summaryGeneratingListener.getSummary();
            LOGGER.info("Tests found: {} , Succeeded: {}, Failures: {}, Aborted: {}, Skipped: {}" ,
                    summary.getTestsFoundCount(), summary.getTestsSucceededCount(), summary.getTestsFailedCount(),
                    summary.getTestsAbortedCount(), summary.getTestsSkippedCount());

            List<String> notStarted = testClassesAndMethods.stream()
                    .filter(s -> !myListener.started.contains(s)).collect(Collectors.toList());
            List<String> notFinished = testClassesAndMethods.stream()
                    .filter(s -> !myListener.finished.contains(s)).collect(Collectors.toList());

            notStarted.forEach(s -> LOGGER.debug("not started test: {}", s));
            LOGGER.debug("not started {}", notStarted.size());
            //notFinished.forEach(s -> LOGGER.info("not finished test: {}", s));
            LOGGER.debug("not finished {}", notFinished.size());
        } catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }


    }

    private static class MyListener implements TestExecutionListener {

        private List<String> started = new CopyOnWriteArrayList<>();
        private List<String> finished = new CopyOnWriteArrayList<>();
        private final EngineExecutionListener executionListener;
        private final JupiterConfiguration jupiterConfiguration;
        private final UniqueId uniqueId;


        public MyListener(EngineExecutionListener executionListener, JupiterConfiguration jupiterConfiguration, UniqueId uniqueId) {
            this.executionListener = executionListener;
            this.jupiterConfiguration = jupiterConfiguration;
            this.uniqueId = uniqueId;
        }

        @Override
        public void testPlanExecutionStarted(TestPlan testPlan) {
            //
        }

        @Override
        public void testPlanExecutionFinished(TestPlan testPlan) {
            //
        }

        @Override
        public void dynamicTestRegistered(TestIdentifier testIdentifier) {

            if(testIdentifier.isTest()) {
                TestSource testSource = testIdentifier.getSource().get();
                if(testSource instanceof MethodSource) {
                    MethodSource methodSource = (MethodSource) testSource;
                    executionListener.dynamicTestRegistered(new TestMethodTestDescriptor(uniqueId, methodSource.getJavaClass(), methodSource.getJavaMethod(),
                            this.jupiterConfiguration));
                }
            }
        }

        @Override
        public void executionSkipped(TestIdentifier testIdentifier, String reason) {
            if(testIdentifier.isTest()) {
                TestSource testSource = testIdentifier.getSource().get();
                if(testSource instanceof MethodSource) {
                    MethodSource methodSource = (MethodSource) testSource;
                    LOGGER.info("Skipped Test:{}#{}",
                            methodSource.getClassName(),
                            methodSource.getMethodName());
                    executionListener.executionSkipped(new TestMethodTestDescriptor(uniqueId, methodSource.getJavaClass(), methodSource.getJavaMethod(),
                            this.jupiterConfiguration), reason);
                }
            }
        }

        @Override
        public void executionStarted(TestIdentifier testIdentifier) {
            if(testIdentifier.isTest()) {
                TestSource testSource = testIdentifier.getSource().get();
                if(testSource instanceof MethodSource) {
                    MethodSource methodSource = (MethodSource) testSource;
                    LOGGER.info("Running Test:{}#{}",
                            methodSource.getClassName(),
                            methodSource.getMethodName());
                    started.add(methodSource.getClassName() + "#" + methodSource.getMethodName());
                    executionListener.executionStarted(new TestMethodTestDescriptor(uniqueId, methodSource.getJavaClass(), methodSource.getJavaMethod(),
                            this.jupiterConfiguration));
                }
            }
        }

        @Override
        public void reportingEntryPublished(TestIdentifier testIdentifier, ReportEntry entry) {
            //
            //executionListener.reportingEntryPublished();
        }

        @Override
        public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
            // skip root
            if (!testIdentifier.getParentId().isPresent()){
                return;
            }
            if (testIdentifier.isTest()) {
                TestSource testSource = testIdentifier.getSource().get();
                if(testSource instanceof MethodSource) {
                    MethodSource methodSource = (MethodSource) testSource;
                    LOGGER.info("Finish test:{}#{}, status: {}, throwable: {}",
                            methodSource.getClassName(),
                            methodSource.getMethodName(),
                            testExecutionResult.getStatus().toString(),
                            testExecutionResult.getThrowable().isPresent()?testExecutionResult.getThrowable():"");
                    finished.add(methodSource.getClassName()+"#"+methodSource.getMethodName());
                    executionListener.executionFinished(new TestMethodTestDescriptor(uniqueId, methodSource.getJavaClass(), methodSource.getJavaMethod(),
                            this.jupiterConfiguration), testExecutionResult);
                }
            }
        }
    }


}
