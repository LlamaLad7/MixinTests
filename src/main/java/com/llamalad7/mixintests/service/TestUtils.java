package com.llamalad7.mixintests.service;

import com.llamalad7.mixintests.harness.SandboxInfo;
import org.spongepowered.asm.mixin.MixinEnvironment;

@SuppressWarnings("unused")
public class TestUtils {
    public static void doTest() {
        MixinEnvironment.getCurrentEnvironment().audit();
    }

    public static SandboxInfo getSandboxInfo() {
        return TransformingClassLoader.getInstance().getSandboxInfo();
    }
}
