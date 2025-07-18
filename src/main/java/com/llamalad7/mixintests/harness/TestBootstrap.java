package com.llamalad7.mixintests.harness;

import com.github.zafarkhaja.semver.Version;
import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.golden.GoldenTest;
import com.llamalad7.mixintests.harness.tests.TestBox;
import com.llamalad7.mixintests.harness.tests.TestFilterer;
import com.llamalad7.mixintests.harness.util.DirectoryPruner;
import com.llamalad7.mixintests.harness.util.MixinVersionInfo;
import com.llamalad7.mixintests.harness.util.MixinVersions;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.DynamicTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class TestBootstrap {
    private static final Set<Path> outputPaths = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private static boolean testsFailed = false;

    public static Stream<DynamicTest> doTest(String testName, String configName, Class<?> testClass) {
        try {
            Object testInstance;
            try {
                testInstance = testClass.getConstructor().newInstance();
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
            return getMixinVersions()
                    .filter(versions -> TestFilterer.shouldRun(versions, testInstance))
                    .map(versions -> DynamicTest.dynamicTest(versions.toString(), () -> {
                        try {
                            doTest(testName, configName, testInstance, versions);
                        } catch (Throwable t) {
                            testsFailed = true;
                            throw t;
                        }
                    }));
        } catch (Throwable t) {
            testsFailed = true;
            throw t;
        }
    }

    public static void beforeTests() {
        cleanMixinOut();
    }

    public static void afterTests() {
        if (!testsFailed) {
            cleanStaleOutputs();
        }
    }

    private static void doTest(String testName, String configName, Object testInstance, MixinVersions mixinVersions) {
        MixinTest testAnn = testInstance.getClass().getAnnotation(MixinTest.class);
        Class<? extends TestBox> boxClass = testAnn.box();
        TestResult result;
        try (Sandbox sandbox = new Sandbox(configName, mixinVersions)) {
            result = sandbox.doTest(() -> {
                TestBox box;
                try {
                    box = (TestBox) sandbox.loadClass(boxClass.getName()).getConstructor().newInstance();
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
                return box.run();
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        new GoldenTest(testName, result, mixinVersions, testAnn.testBytecode()).test();
    }

    private static Stream<MixinVersions> getMixinVersions() {
        Set<MixinVersions> result = new HashSet<>();
        for (Version mixinVersion : MixinVersionInfo.MIXIN_VERSIONS) {
            result.add(new MixinVersions(mixinVersion, MixinVersionInfo.MIXINEXTRAS_VERSIONS.last(), false));
        }
        for (Version mixinVersion : MixinVersionInfo.FABRIC_MIXIN_VERSIONS) {
            result.add(new MixinVersions(mixinVersion, MixinVersionInfo.MIXINEXTRAS_VERSIONS.last(), true));
        }
        for (Version mixinExtrasVersion : MixinVersionInfo.MIXINEXTRAS_VERSIONS) {
            result.add(new MixinVersions(MixinVersionInfo.MIXIN_VERSIONS.last(), mixinExtrasVersion, false));
            result.add(new MixinVersions(MixinVersionInfo.FABRIC_MIXIN_VERSIONS.last(), mixinExtrasVersion, true));
        }
        result.add(new MixinVersions(MixinVersionInfo.MIXIN_VERSIONS.last(), null, false));
        result.add(new MixinVersions(MixinVersionInfo.FABRIC_MIXIN_VERSIONS.last(), null, true));
        return result.stream();
    }

    private static void cleanMixinOut() {
        try {
            FileUtils.deleteDirectory(new File(".mixin.out"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void touchedOutput(Path path) {
        outputPaths.add(path);
    }

    private static void cleanStaleOutputs() {
        if (BuildConstants.TESTS_FILTERED) {
            return;
        }
        try {
            new DirectoryPruner(outputPaths).prune(Path.of(BuildConstants.TEST_OUTPUT_DIR));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
