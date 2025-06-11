package com.llamalad7.mixintests.harness;

import com.llamalad7.mixintests.harness.util.MixinVersions;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URLClassLoader;
import java.util.function.Supplier;

public class Sandbox implements Closeable {
    private final URLClassLoader transformingClassLoader;

    public Sandbox(String configName, MixinVersions mixinVersions) {
        this.transformingClassLoader = makeTransformingClassLoader(configName, mixinVersions);
    }

    public Class<?> loadClass(String className) throws ClassNotFoundException {
        return transformingClassLoader.loadClass(className);
    }

    public TestResult doTest(Supplier<String> routine) {
        ClassLoader currentThreadPreviousClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(transformingClassLoader);
        try {
            return new TestResult(routine.get(), ((TransformingClassLoaderBridge) transformingClassLoader).getTransformedClasses());
        } finally {
            Thread.currentThread().setContextClassLoader(currentThreadPreviousClassLoader);
        }
    }

    public void close() throws IOException {
        if (transformingClassLoader != null) {
            transformingClassLoader.close();
        }
    }

    private static URLClassLoader makeTransformingClassLoader(String configName, MixinVersions mixinVersions) {
        try {
            URLClassLoader bootstrapCl = new IsolatedClassLoader("bootstrap", ClassLoader.getSystemClassLoader(), mixinVersions);
            Class<?> transformingClClass = bootstrapCl.loadClass("com.llamalad7.mixintests.service.TransformingClassLoader");
            Constructor<?> ctor = transformingClClass.getConstructor(ClassLoader.class, SandboxInfo.class);
            return (URLClassLoader) ctor.newInstance(bootstrapCl, new SandboxInfo(configName, mixinVersions));
        } catch (Exception e) {
            throw new RuntimeException("Failed to make sandbox CL: ", e);
        }
    }
}
