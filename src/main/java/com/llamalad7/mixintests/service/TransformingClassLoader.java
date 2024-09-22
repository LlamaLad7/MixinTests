package com.llamalad7.mixintests.service;

import com.llamalad7.mixintests.harness.IsolatedClassLoader;

import java.io.IOException;

public class TransformingClassLoader extends IsolatedClassLoader {
    static {
        registerAsParallelCapable();
    }

    private static final String TARGET_PACKAGE = "com.llamalad7.mixintests.tests.";

    public TransformingClassLoader(ClassLoader parent, String testClass) {
        super("transforming", parent);
        MixinSetup.init(testClass);
    }

    @Override
    protected boolean shouldLoad(String className) {
        return className.startsWith(TARGET_PACKAGE);
    }

    @Override
    protected byte[] getClassBytes(String name) {
        byte[] classBytes = super.getClassBytes(name);
        if (classBytes == null) {
            return null;
        }
        return TestMixinService.transformer.transformClassBytes(name, name, classBytes);
    }

    @Override
    public void close() throws IOException {
        super.close();
        ((IsolatedClassLoader) delegate).close();
    }
}
