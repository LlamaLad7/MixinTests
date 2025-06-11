package com.llamalad7.mixintests.harness;

import java.util.Map;

public interface TransformingClassLoaderBridge {
    Map<String, byte[]> getTransformedClasses();
}
