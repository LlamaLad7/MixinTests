package com.llamalad7.mixintests.tests.mixins.fabric.targeterrors;

import com.llamalad7.mixintests.ap.annotations.Config;
import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.ap.annotations.TestOption;
import com.llamalad7.mixintests.tests.targets.EmptyTarget;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Ensures that unresolved target errors remain non-fatal on non-required mixin configs.
 */
@MixinTest(box = EmptyTarget.class, fabricMixin = TestOption.ON, minFabricMixin = "0.17.4")
public class NonRequiredConfigTest {
    @Config(required = false)
    @Mixin(targets = "mixintests.ThisClassIsNotReal")
    static abstract class NonRequiredConfig {
    }
}
