package com.llamalad7.mixintests.service;

import java.util.Random;
import java.util.UUID;

@SuppressWarnings("unused")
public class MixinHooks {
    private static final Random RANDOM = new Random(0);

    public static UUID randomUUID() {
        return new UUID(RANDOM.nextLong(), RANDOM.nextLong());
    }

    public static String getMixinOutputDir() {
        return ".mixin.out/" + SandboxInfo.getInstance().mixinConfig();
    }
}
