package com.llamalad7.mixintests.harness;

public class TestBootstrap {
    private static final String TEST_UTILS = "com.llamalad7.mixintests.service.TestUtils";
    private static final String DO_TEST = "doTest";

    public static void doTest(String configName) {
        Sandbox sandbox = new Sandbox(configName);
        sandbox.withContextClassLoader(() -> {
            try {
                sandbox.loadClass(TEST_UTILS).getMethod(DO_TEST).invoke(null);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
