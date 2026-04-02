package com.llamalad7.mixintests.service;

import com.github.zafarkhaja.semver.Version;
import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import com.llamalad7.mixintests.harness.MixinConfig;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.FabricUtil;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.transformer.Config;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MixinSetup {
    private static final Version NEW_CONFIG_VERSION = Version.parse("0.8.6");

    public static void init(List<MixinConfig> configs) {
        MixinBootstrap.init();
        if (TestUtils.getSandboxInfo().mixinVersions.hasMixinExtras()) {
            MixinExtrasBootstrap.init();
        }
        for (MixinConfig config : configs) {
            addMixinConfig(config);
        }
        if (TestUtils.getSandboxInfo().mixinVersions.isFabric) {
            decorateWithFabricCompat(configs);
        }
        finishMixinBootstrapping();
    }

    private static void addMixinConfig(MixinConfig config) {
        Version version = TestUtils.getSandboxInfo().mixinVersions.upstreamMixinVersion();
        if (version.isHigherThanOrEquivalentTo(NEW_CONFIG_VERSION)) {
            Mixins.addConfiguration(config.json, null);
        } else {
            Mixins.addConfiguration(config.json);
        }
    }

    private static void decorateWithFabricCompat(List<MixinConfig> configs) {
        Map<String, Integer> compatMap = configs.stream()
                .filter(it -> it.fabricCompat != null)
                .collect(Collectors.toMap(it -> it.json, it -> it.fabricCompat));
        for (Config config : Mixins.getConfigs()) {
            if (compatMap.containsKey(config.getName())) {
                config.getConfig().decorate(FabricUtil.KEY_COMPATIBILITY, compatMap.get(config.getName()));
            }
        }
    }

    private static void finishMixinBootstrapping() {
        try {
            Method m = MixinEnvironment.class.getDeclaredMethod("gotoPhase", MixinEnvironment.Phase.class);
            m.setAccessible(true);
            m.invoke(null, MixinEnvironment.Phase.INIT);
            m.invoke(null, MixinEnvironment.Phase.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
