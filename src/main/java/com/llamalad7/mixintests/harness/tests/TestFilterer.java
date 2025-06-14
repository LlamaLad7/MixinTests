package com.llamalad7.mixintests.harness.tests;

import com.llamalad7.mixintests.ap.annotations.MixinTestGroup;
import com.llamalad7.mixintests.harness.util.MixinVersions;

public class TestFilterer {
    public static boolean shouldRun(MixinVersions versions, Object testInstance) {
        if (testInstance instanceof RestrictedTest restricted) {
            return restricted.shouldRun(versions);
        }
        return shouldRun(versions, testInstance.getClass().getAnnotation(MixinTestGroup.class));
    }

    public static boolean shouldRun(MixinVersions versions, MixinTestGroup ann) {
        return true;
    }
}
