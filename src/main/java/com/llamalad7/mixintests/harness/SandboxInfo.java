package com.llamalad7.mixintests.harness;

import com.llamalad7.mixintests.harness.util.MixinVersions;

import java.util.List;

public final class SandboxInfo {
    public final String testName;
    public final List<MixinConfig> mixinConfigs;
    public final MixinVersions mixinVersions;
    public final String[] mixinOptions;

    public SandboxInfo(String testName, List<MixinConfig> mixinConfigs, MixinVersions mixinVersions, String[] mixinOptions) {
        this.testName = testName;
        this.mixinConfigs = mixinConfigs;
        this.mixinVersions = mixinVersions;
        this.mixinOptions = mixinOptions;
    }
}
