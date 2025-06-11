package com.llamalad7.mixintests.harness;

import com.github.zafarkhaja.semver.Version;
import com.llamalad7.mixintests.harness.util.MixinVersionInfo;
import com.llamalad7.mixintests.harness.util.MixinVersions;
import org.junit.jupiter.api.DynamicTest;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class TestBootstrap {
    private static final String TEST_UTILS = "com.llamalad7.mixintests.service.TestUtils";
    private static final String DO_TEST = "doTest";

    public static Stream<DynamicTest> doTest(String configName) {
        return getMixinVersions()
                .map(versions -> DynamicTest.dynamicTest(versions.toString(), () -> {
                    doTest(configName, versions);
                }));
    }

    private static void doTest(String configName, MixinVersions mixinVersions) {
        Sandbox sandbox = new Sandbox(configName, mixinVersions);
        sandbox.withContextClassLoader(() -> {
            try {
                sandbox.loadClass(TEST_UTILS).getMethod(DO_TEST).invoke(null);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        });
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
        return result.stream();
    }

}
