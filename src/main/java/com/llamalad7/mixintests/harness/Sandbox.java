package com.llamalad7.mixintests.harness;

import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.net.URLClassLoader;

public class Sandbox {
    private final URLClassLoader transformingClassLoader;

    public Sandbox(String testClassName) throws Exception {
        transformingClassLoader = makeTransformingClassLoader(testClassName);
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
        transformingClassLoader.close();
    }

    private static URLClassLoader makeTransformingClassLoader(String testClassName) throws Exception {
        URLClassLoader bootstrapCl = new IsolatedClassLoader("bootstrap", ClassLoader.getSystemClassLoader());
        Class<?> transformingClClass = bootstrapCl.loadClass("com.llamalad7.mixintests.service.TransformingClassLoader");
        Constructor<?> ctor = transformingClClass.getConstructor(ClassLoader.class, String.class);
        return (URLClassLoader) ctor.newInstance(bootstrapCl, testClassName);
    }
}
