package com.llamalad7.mixintests.harness;

import com.llamalad7.mixintests.harness.util.MixinVersionInfo;
import com.llamalad7.mixintests.harness.util.MixinVersions;
import org.junit.jupiter.api.DynamicTest;

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
        return Stream.concat(MixinVersionInfo.MIXIN_VERSIONS.stream(), MixinVersionInfo.FABRIC_MIXIN_VERSIONS.stream())
                .flatMap(mixinVer ->
                        MixinVersionInfo.MIXINEXTRAS_VERSIONS.stream().map(mixinExtrasVer -> new MixinVersions(mixinVer, mixinExtrasVer))
                );
    }

}
