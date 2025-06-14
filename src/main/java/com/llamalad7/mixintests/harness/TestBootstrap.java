package com.llamalad7.mixintests.harness;

import com.github.zafarkhaja.semver.Version;
import com.llamalad7.mixintests.ap.annotations.MixinTestGroup;
import com.llamalad7.mixintests.golden.GoldenTest;
import com.llamalad7.mixintests.harness.tests.TestBox;
import com.llamalad7.mixintests.harness.tests.TestFilterer;
import com.llamalad7.mixintests.harness.util.MixinVersionInfo;
import com.llamalad7.mixintests.harness.util.MixinVersions;
import org.junit.jupiter.api.DynamicTest;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class TestBootstrap {
    public static Stream<DynamicTest> doTest(String testName, String configName, Class<?> testClass) {
        Object testInstance;
        try {
            testInstance = testClass.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
        return getMixinVersions()
                .filter(versions -> TestFilterer.shouldRun(versions, testInstance))
                .map(versions -> DynamicTest.dynamicTest(versions.toString(), () -> {
                    doTest(testName, configName, testInstance, versions);
                }));
    }

    private static void doTest(String testName, String configName, Object testInstance, MixinVersions mixinVersions) {
        Class<? extends TestBox> boxClass = testInstance.getClass().getAnnotation(MixinTestGroup.class).box();
        TestResult result;
        try (Sandbox sandbox = new Sandbox(configName, mixinVersions)) {
            result = sandbox.doTest(() -> {
                TestBox box;
                try {
                    box = (TestBox) sandbox.loadClass(boxClass.getName()).getConstructor().newInstance();
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
                return box.box();
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        new GoldenTest(testName, result, mixinVersions).test();
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

}
