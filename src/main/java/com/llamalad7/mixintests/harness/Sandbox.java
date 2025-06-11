package com.llamalad7.mixintests.harness;

import com.llamalad7.mixintests.harness.util.MixinVersions;
import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.net.URLClassLoader;

public class Sandbox {
    private final URLClassLoader transformingClassLoader;

    public Sandbox(String configName, MixinVersions mixinVersions) {
        this.transformingClassLoader = makeTransformingClassLoader(configName, mixinVersions);
    }

    public Class<?> loadClass(String className) throws ClassNotFoundException {
        return transformingClassLoader.loadClass(className);
    }

    public Object newInstance(String testClassName) throws Exception {
        return ReflectionUtils.newInstance(
                transformingClassLoader.loadClass(testClassName)
        );
    }

    public void withContextClassLoader(Runnable routine) {
        ClassLoader currentThreadPreviousClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(transformingClassLoader);
        try {
            routine.run();
        } finally {
            Thread.currentThread().setContextClassLoader(currentThreadPreviousClassLoader);
        }
    }

    public void close() throws Exception {
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
