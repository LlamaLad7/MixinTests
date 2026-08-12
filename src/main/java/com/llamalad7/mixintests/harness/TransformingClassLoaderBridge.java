package com.llamalad7.mixintests.harness;

import java.util.Map;

public interface TransformingClassLoaderBridge {
    void initMixin();

    Map<String, byte[]> getTransformedClasses();
}
