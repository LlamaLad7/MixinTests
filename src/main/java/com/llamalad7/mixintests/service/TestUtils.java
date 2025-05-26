package com.llamalad7.mixintests.service;

import org.spongepowered.asm.mixin.MixinEnvironment;

@SuppressWarnings("unused")
public class TestUtils {
    public static void doTest() {
        MixinEnvironment.getCurrentEnvironment().audit();
    }
}
