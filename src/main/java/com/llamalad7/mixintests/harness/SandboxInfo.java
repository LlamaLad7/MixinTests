package com.llamalad7.mixintests.harness;

import com.llamalad7.mixintests.harness.util.MixinVersions;

public final class SandboxInfo {
    public final String mixinConfig;
    public final MixinVersions mixinVersions;

    public SandboxInfo(String mixinConfig, MixinVersions mixinVersions) {
        this.mixinConfig = mixinConfig;
        this.mixinVersions = mixinVersions;
    }
}
