package com.llamalad7.mixintests.service;

import com.llamalad7.mixintests.harness.SandboxInfo;

@SuppressWarnings("unused")
public class TestUtils {
    public static SandboxInfo getSandboxInfo() {
        return TransformingClassLoader.getInstance().getSandboxInfo();
    }
}
