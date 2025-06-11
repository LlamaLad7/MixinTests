package com.llamalad7.mixintests.service;

import com.github.zafarkhaja.semver.Version;
import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.lang.reflect.Method;

public class MixinSetup {
    private static final Version NEW_CONFIG_VERSION = Version.parse("0.8.6");

    public static void init(String configName) {
        MixinBootstrap.init();
        MixinExtrasBootstrap.init();
        addMixinConfig(configName);
        finishMixinBootstrapping();
    }

    private static void addMixinConfig(String configName) {
        Version version = TestUtils.getSandboxInfo().mixinVersions().upstreamMixinVersion();
        if (version.isHigherThanOrEquivalentTo(NEW_CONFIG_VERSION)) {
            Mixins.addConfiguration(configName, null);
        } else {
            Mixins.addConfiguration(configName);
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
