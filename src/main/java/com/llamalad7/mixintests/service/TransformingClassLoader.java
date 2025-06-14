package com.llamalad7.mixintests.service;

import com.llamalad7.mixintests.harness.IsolatedClassLoader;
import com.llamalad7.mixintests.harness.SandboxInfo;
import com.llamalad7.mixintests.harness.TransformingClassLoaderBridge;
import com.llamalad7.mixintests.tests.targets.DummyMixinTarget;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TransformingClassLoader extends IsolatedClassLoader implements TransformingClassLoaderBridge {
    static {
        registerAsParallelCapable();
    }

    private static final List<String> TARGET_PACKAGES = List.of(
            "com.llamalad7.mixintests.tests.",
            MIXIN_SYNTHETIC_PACKAGE
    );

    private static TransformingClassLoader INSTANCE;

    private final SandboxInfo sandboxInfo;
    private final Map<String, byte[]> postMixinClasses = new ConcurrentHashMap<>();

    public TransformingClassLoader(ClassLoader parent, SandboxInfo sandboxInfo) {
        super("transforming", parent, null);
        if (INSTANCE != null) {
            throw new IllegalStateException("TransformingClassLoader already initialized");
        }
        INSTANCE = this;
        this.sandboxInfo = sandboxInfo;
        MixinSetup.init(sandboxInfo.mixinConfig());
        loadDummyTarget();
    }

    @Override
    protected boolean shouldLoad(String className) {
        return TARGET_PACKAGES.stream().anyMatch(className::startsWith);
    }

    @Override
    protected byte[] getClassBytes(String name) {
        byte[] classBytes = super.getClassBytes(name);
        byte[] transformedBytes = TestMixinService.transformer.transformClassBytes(name, name, classBytes);
        if (classBytes != null && transformedBytes != classBytes) {
            postMixinClasses.putIfAbsent(name, transformedBytes);
        }
        return transformedBytes;
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

    public Map<String, byte[]> getTransformedClasses() {
        return Collections.unmodifiableMap(postMixinClasses);
    }
}
