package com.llamalad7.mixintests.harness;

import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.net.URLClassLoader;

public class Sandbox {
    private final String testClassName;
    private URLClassLoader transformingClassLoader;

    public Sandbox(String testClassName) {
        this.testClassName = testClassName;
    }

    public Object newInstance(String testClassName) throws Exception {
        return ReflectionUtils.newInstance(
                getTransformingClassLoader().loadClass(testClassName)
        );
    }

    public void withContextClassLoader(Runnable routine) {
        ClassLoader currentThreadPreviousClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(getTransformingClassLoader());
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

    private synchronized URLClassLoader getTransformingClassLoader() {
        if (transformingClassLoader == null) {
            transformingClassLoader = makeTransformingClassLoader(testClassName);
        }
        return transformingClassLoader;
    }

    private static URLClassLoader makeTransformingClassLoader(String testClassName) {
        try {
            URLClassLoader bootstrapCl = new IsolatedClassLoader("bootstrap", ClassLoader.getSystemClassLoader());
            Class<?> transformingClClass = bootstrapCl.loadClass("com.llamalad7.mixintests.service.TransformingClassLoader");
            Constructor<?> ctor = transformingClClass.getConstructor(ClassLoader.class, String.class);
            return (URLClassLoader) ctor.newInstance(bootstrapCl, testClassName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to make sandbox CL: ", e);
        }
    }
}
