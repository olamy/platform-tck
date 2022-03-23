package jakarta.tck.junit5;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.engine.JupiterTestEngine;
import org.junit.jupiter.engine.config.CachingJupiterConfiguration;
import org.junit.jupiter.engine.config.DefaultJupiterConfiguration;
import org.junit.jupiter.engine.config.JupiterConfiguration;
import org.junit.jupiter.engine.descriptor.ClassBasedTestDescriptor;
import org.junit.jupiter.engine.descriptor.ClassTestDescriptor;
import org.junit.jupiter.engine.descriptor.JupiterEngineDescriptor;
import org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor;
import org.junit.jupiter.engine.discovery.predicates.IsTestClassWithTests;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestEngine;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.discovery.ClassSelector;
import org.junit.platform.engine.discovery.ClasspathResourceSelector;
import org.junit.platform.engine.discovery.ClasspathRootSelector;
import org.junit.platform.engine.discovery.DirectorySelector;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.discovery.FileSelector;
import org.junit.platform.engine.discovery.MethodSelector;
import org.junit.platform.engine.discovery.ModuleSelector;
import org.junit.platform.engine.discovery.NestedClassSelector;
import org.junit.platform.engine.discovery.NestedMethodSelector;
import org.junit.platform.engine.discovery.PackageSelector;
import org.junit.platform.engine.discovery.UniqueIdSelector;
import org.junit.platform.engine.discovery.UriSelector;
import org.junit.platform.engine.support.discovery.EngineDiscoveryRequestResolver;
import org.junit.platform.engine.support.discovery.SelectorResolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;
import static org.junit.platform.commons.util.ReflectionUtils.findMethods;
import static org.junit.platform.engine.support.discovery.SelectorResolver.Resolution.unresolved;

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
        JupiterConfiguration configuration = new CachingJupiterConfiguration(
                new DefaultJupiterConfiguration(request.getConfigurationParameters()));

        JupiterEngineDescriptor engineDescriptor = new JupiterEngineDescriptor(uniqueId, configuration);

        EngineDiscoveryRequestResolver.builder()
                .addClassContainerSelectorResolver(new IsTestClassWithTests())
                .addSelectorResolver(context -> new TckTestsResolver(testClassesAndMethods, uniqueId, configuration, context.getClassNameFilter()))
                .build()
                .resolve(request, engineDescriptor);

        return engineDescriptor;

    }

    private static class TckTestsResolver implements SelectorResolver {

        private final List<String> testClassesAndMethods;
        private final UniqueId uniqueId;
        private final JupiterConfiguration configuration;
        private final Predicate<String> classNameFilter;
        private static final IsTestClassWithTests isTestClassWithTests = new IsTestClassWithTests();

        private TckTestsResolver(List<String> testClassesAndMethods, UniqueId uniqueId,  JupiterConfiguration configuration, Predicate<String> classNameFilter) {
            this.testClassesAndMethods = testClassesAndMethods;
            this.uniqueId = uniqueId;
            this.configuration = configuration;
            this.classNameFilter = classNameFilter;
        }

        @Override
        public Resolution resolve(ClassSelector selector, Context context) {
            Class<?> testClass = selector.getJavaClass();
            if (isTestClassWithTests.test(testClass) && classNameFilter.test(testClass.getName())) {
                return toResolution(
                        context.addToParent(parent -> Optional.of(newClassTestDescriptor(parent, testClass))));
            }

            return unresolved();
        }

        private ClassTestDescriptor newClassTestDescriptor(TestDescriptor parent, Class<?> testClass) {
            return new ClassTestDescriptor(
                    parent.getUniqueId().append(ClassTestDescriptor.SEGMENT_TYPE, testClass.getName()), testClass,
                    configuration);
        }

        private Resolution toResolution(Optional<? extends ClassBasedTestDescriptor> testDescriptor) {
            Optional<Resolution> opt = testDescriptor.map(td -> {
                Class<?> testClass = td.getTestClass();
                return Resolution.match(Match.exact(td, () -> {
                    Stream<DiscoverySelector> methods = findMethods(testClass, METHODS_PREDICATE).stream()
                            .map(method -> DiscoverySelectors.selectMethod(testClass, method));
                    return methods.collect(toCollection((Supplier<Set<DiscoverySelector>>) LinkedHashSet::new));
                }));
            });
            return opt.orElse(unresolved());
        }

        private final Predicate<Method> METHODS_PREDICATE = method -> {
            String fqcn = method.getDeclaringClass().getName() + "#" + method.getName();
            return method.getAnnotation(Test.class) != null &&
                    TckTestsResolver.this.testClassesAndMethods.contains(fqcn);
        };


        @Override
        public Resolution resolve(ClasspathResourceSelector selector, Context context) {
            return SelectorResolver.super.resolve(selector, context);
        }

        @Override
        public Resolution resolve(ClasspathRootSelector selector, Context context) {
            return SelectorResolver.super.resolve(selector, context);
        }

        @Override
        public Resolution resolve(NestedClassSelector selector, Context context) {
            return SelectorResolver.super.resolve(selector, context);
        }

        @Override
        public Resolution resolve(DirectorySelector selector, Context context) {
            return SelectorResolver.super.resolve(selector, context);
        }

        @Override
        public Resolution resolve(FileSelector selector, Context context) {
            return SelectorResolver.super.resolve(selector, context);
        }

        @Override
        public Resolution resolve(NestedMethodSelector selector, Context context) {
            return SelectorResolver.super.resolve(selector, context);
        }

        @Override
        public Resolution resolve(ModuleSelector selector, Context context) {
            return SelectorResolver.super.resolve(selector, context);
        }

        @Override
        public Resolution resolve(PackageSelector selector, Context context) {
            return SelectorResolver.super.resolve(selector, context);
        }

        @Override
        public Resolution resolve(UniqueIdSelector selector, Context context) {
            return SelectorResolver.super.resolve(selector, context);
        }

        @Override
        public Resolution resolve(UriSelector selector, Context context) {
            return SelectorResolver.super.resolve(selector, context);
        }

        @Override
        public Resolution resolve(DiscoverySelector selector, Context context) {
            return SelectorResolver.super.resolve(selector, context);
        }

        @Override
        public Resolution resolve(MethodSelector selector, Context context) {
            String fqcn = selector.getClassName() + "#" + selector.getMethodName();
            if (this.testClassesAndMethods.contains(fqcn)) {
                TestMethodTestDescriptor testDescriptor = new TestMethodTestDescriptor(uniqueId, selector.getJavaClass(), selector.getJavaMethod(), configuration);
                return SelectorResolver.Resolution.match(Match.exact(testDescriptor));
            }
            return SelectorResolver.Resolution.unresolved();
        }
    }


    @Override
    public void execute(ExecutionRequest executionRequest) {
        new JupiterTestEngine().execute(executionRequest);
    }

}
