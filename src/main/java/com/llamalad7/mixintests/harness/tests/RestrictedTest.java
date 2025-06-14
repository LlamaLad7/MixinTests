package com.llamalad7.mixintests.harness.tests;

import com.llamalad7.mixintests.ap.annotations.MixinTestGroup;
import com.llamalad7.mixintests.harness.util.MixinVersions;

public interface RestrictedTest {
    default boolean shouldRun(MixinVersions versions) {
        return TestFilterer.shouldRun(versions, getClass().getAnnotation(MixinTestGroup.class));
    }
}
