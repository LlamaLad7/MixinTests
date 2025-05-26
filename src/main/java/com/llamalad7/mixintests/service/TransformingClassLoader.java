package com.llamalad7.mixintests.service;

import com.llamalad7.mixintests.harness.IsolatedClassLoader;
import com.llamalad7.mixintests.tests.targets.DummyMixinTarget;

import java.io.IOException;

public class TransformingClassLoader extends IsolatedClassLoader {
    static {
        registerAsParallelCapable();
    }

    private static final String TARGET_PACKAGE = "com.llamalad7.mixintests.tests.";

    private static TransformingClassLoader INSTANCE;

    private final SandboxInfo sandboxInfo;

    public TransformingClassLoader(ClassLoader parent, String configName) {
        super("transforming", parent);
        if (INSTANCE != null) {
            throw new IllegalStateException("TransformingClassLoader already initialized");
        }
        INSTANCE = this;
        this.sandboxInfo = new SandboxInfo(configName);
        MixinSetup.init(configName);
        loadDummyTarget();
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

    // This triggers mixin selection
    private void loadDummyTarget() {
        try {
            loadClass(DummyMixinTarget.NAME);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static TransformingClassLoader getInstance() {
        return INSTANCE;
    }

    public SandboxInfo getSandboxInfo() {
        return sandboxInfo;
    }
}
