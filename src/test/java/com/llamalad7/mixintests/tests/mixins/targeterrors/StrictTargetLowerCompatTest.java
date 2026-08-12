package com.llamalad7.mixintests.tests.mixins.targeterrors;

import com.llamalad7.mixintests.ap.annotations.Config;
import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.tests.targets.EmptyTarget;
import org.spongepowered.asm.mixin.FabricUtil;
import org.spongepowered.asm.mixin.Mixin;

/**
 * See {@link StrictTargetHigherCompatTest}
 */
@MixinTest(box = EmptyTarget.class, shouldFail = true, mixinOptions = "DEBUG_TARGETS=true")
public class StrictTargetLowerCompatTest {
    @Config(fabricCompat = FabricUtil.COMPATIBILITY_0_17_1)
    @Mixin(targets = "mixintests.ThisClassDoesNotExist")
    static abstract class StrictTargetLowerCompatMixin {
    }
}
