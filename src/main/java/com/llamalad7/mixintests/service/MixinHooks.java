package com.llamalad7.mixintests.service;

import com.llamalad7.mixintests.harness.SandboxInfo;

import java.util.Random;
import java.util.UUID;

@SuppressWarnings("unused")
public class MixinHooks {
    private static final Random RANDOM = new Random(0);

    public static UUID randomUUID() {
        return new UUID(RANDOM.nextLong(), RANDOM.nextLong());
    }

    public static String getMixinOutputDir() {
        SandboxInfo info = TestUtils.getSandboxInfo();
        return ".mixin.out/" + info.mixinConfig() + '/' + info.mixinVersions().getSlug();
    }
}
