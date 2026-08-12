package com.llamalad7.mixintests.tests.mixins.targeterrors;

import com.llamalad7.mixintests.ap.annotations.Config;
import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.tests.targets.EmptyTarget;
import org.spongepowered.asm.mixin.FabricUtil;
import org.spongepowered.asm.mixin.Mixin;

/**
 * The unresolved target here should not be fatal on any Mixin version, even on newer versions due to the target compat.
 */
@MixinTest(box = EmptyTarget.class)
public class LowerCompatLevelTest {
    @Config(fabricCompat = FabricUtil.COMPATIBILITY_0_17_1)
    @Mixin(targets = "mixintests.ThisClassIsNotReal")
    static abstract class LowerCompatLevel {
    }
}
