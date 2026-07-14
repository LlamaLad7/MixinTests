package com.llamalad7.mixintests.harness;

import com.llamalad7.mixintests.harness.tests.TestBox;
import com.llamalad7.mixintests.harness.util.MixinVersions;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URLClassLoader;
import java.util.List;
import java.util.function.Supplier;

public class Sandbox implements Closeable {
    private final URLClassLoader transformingClassLoader;

    public Sandbox(String testName, List<MixinConfig> configs, MixinVersions mixinVersions) {
        this.transformingClassLoader = makeTransformingClassLoader(testName, configs, mixinVersions);
    }

    public Class<?> loadClass(String className) throws ClassNotFoundException {
        return transformingClassLoader.loadClass(className);
    }

    public TestResult doTest(Supplier<TestBox.Result> routine) {
        ClassLoader currentThreadPreviousClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(transformingClassLoader);
        try {
            TestBox.Result result = routine.get();
            return new TestResult(
                    result.output,
                    result.error,
                    ((TransformingClassLoaderBridge) transformingClassLoader).getTransformedClasses()
            );
        } finally {
            Thread.currentThread().setContextClassLoader(currentThreadPreviousClassLoader);
        }
    }

    public void close() throws IOException {
        if (transformingClassLoader != null) {
            transformingClassLoader.close();
        }
    }

    private static URLClassLoader makeTransformingClassLoader(String testName, List<MixinConfig> configs, MixinVersions mixinVersions) {
        try {
            URLClassLoader bootstrapCl = new IsolatedClassLoader(ClassLoader.getSystemClassLoader(), mixinVersions);
            Class<?> transformingClClass = bootstrapCl.loadClass("com.llamalad7.mixintests.service.TransformingClassLoader");
            Constructor<?> ctor = transformingClClass.getConstructor(ClassLoader.class, SandboxInfo.class);
            return (URLClassLoader) ctor.newInstance(bootstrapCl, new SandboxInfo(testName, configs, mixinVersions));
        } catch (Exception e) {
            throw new RuntimeException("Failed to make sandbox CL: ", e);
        }
    }
}
