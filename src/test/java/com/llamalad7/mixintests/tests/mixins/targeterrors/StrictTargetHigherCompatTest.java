package com.llamalad7.mixintests.tests.mixins.targeterrors;

import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.tests.targets.EmptyTarget;
import org.spongepowered.asm.mixin.Mixin;

/**
 * <p>Unresolved targets should always be fatal if strict target checking is enabled.</p>
 *
 * <p>See {@link StrictTargetLowerCompatTest} for the pre-0.17.4 compat level equivalent test.</p>
 */
@MixinTest(box = EmptyTarget.class, shouldFail = true, mixinOptions = "DEBUG_TARGETS=true")
public class StrictTargetHigherCompatTest {
    @Mixin(targets = "mixintests.ThisClassDoesNotExist")
    static abstract class StrictTargetHigherCompatMixin {
    }
}
