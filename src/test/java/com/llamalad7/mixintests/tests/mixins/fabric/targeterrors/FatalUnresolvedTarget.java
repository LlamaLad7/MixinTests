package com.llamalad7.mixintests.tests.mixins.fabric.targeterrors;

import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.ap.annotations.TestOption;
import com.llamalad7.mixintests.tests.targets.EmptyTarget;
import org.spongepowered.asm.mixin.Mixin;

@MixinTest(box = EmptyTarget.class, shouldFail = true, fabricMixin = TestOption.ON, minFabricMixin = "0.17.4")
public class FatalUnresolvedTarget {
    @Mixin(targets = "mixintests.ThisClassIsNotReal")
    static abstract class RequiredConfig {
    }
}
