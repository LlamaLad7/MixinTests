package com.llamalad7.mixintests.harness.tests;

import com.github.zafarkhaja.semver.Version;
import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.harness.util.MixinVersions;

public class TestFilterer {
    public static boolean shouldRun(MixinVersions versions, Object testInstance) {
        if (testInstance instanceof RestrictedTest restricted) {
            return restricted.shouldRun(versions);
        }
        return shouldRun(versions, testInstance.getClass().getAnnotation(MixinTest.class));
    }

    public static boolean shouldRun(MixinVersions versions, MixinTest ann) {
        if (!ann.fabricMixin().permits(versions.isFabric()) || !ann.mixinExtras().permits(versions.hasMixinExtras())) {
            return false;
        }
        if (versions.isFabric() && !ann.minFabricMixin().isBlank()) {
            if (Version.parse(ann.minFabricMixin()).isHigherThan(versions.mixinVersion())) {
                return false;
            }
        } else if (!ann.minMixin().isBlank() && Version.parse(ann.minMixin()).isHigherThan(versions.upstreamMixinVersion())) {
            return false;
        }
        if (versions.hasMixinExtras() && !ann.minMixinExtras().isBlank() && Version.parse(ann.minMixinExtras()).isHigherThan(versions.mixinExtrasVersion())) {
            return false;
        }
        return true;
    }
}
