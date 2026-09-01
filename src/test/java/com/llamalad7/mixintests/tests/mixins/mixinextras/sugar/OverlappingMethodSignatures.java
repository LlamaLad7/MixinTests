package com.llamalad7.mixintests.tests.mixins.mixinextras.sugar;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.ap.annotations.TestOption;
import com.llamalad7.mixintests.harness.tests.TestBox;
import com.llamalad7.mixintests.tests.targets.EmptyTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Sugar stripping temporarily breaks the invariant that there can be only 1 method with a given name and desc.
 * Ensure the pipeline handles it gracefully.
 */
@MixinTest(box = EmptyTarget.class, mixinExtras = TestOption.ON)
public class OverlappingMethodSignatures {
    @Mixin(EmptyTarget.class)
    static abstract class TheMixin extends TestBox {
        @Inject(method = "box", at = @At("HEAD"))
        private void sameName(CallbackInfo ci, @Share("a") LocalIntRef a) {
            print("A: " + a.get());
        }

        @Inject(method = "box", at = @At("HEAD"))
        private void sameName(CallbackInfo ci, @Share("b") LocalFloatRef b) {
            print("B: " + b.get());
        }
    }
}
