package com.llamalad7.mixintests.tests.mixins.targeterrors;

import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.tests.targets.EmptyTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

/**
 * Unresolved target should not cause a fatal error on any Mixin version with Pseudo
 */
@MixinTest(box = EmptyTarget.class)
public class PseudoMixinTest {
    @Pseudo
    @Mixin(targets = "mixintests.ThisClassIsNotReal")
    static abstract class PseudoMixin {
    }
}
