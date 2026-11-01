package com.llamalad7.mixintests.tests.mixins.fabric.merging;

import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.ap.annotations.TestOption;
import com.llamalad7.mixintests.tests.targets.SignatureTarget;
import org.spongepowered.asm.mixin.Mixin;

@MixinTest(box = SignatureTarget.class, fabricMixin = TestOption.ON, minFabricMixin = "0.17.6", testBytecode = true)
public class SignaturePreservationTest {
    @Mixin({
            SignatureTarget.A.class,
            SignatureTarget.B.class,
            SignatureTarget.C.class,
    })
    static class TheMixin {
    }
}
