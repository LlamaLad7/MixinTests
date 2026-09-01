package com.llamalad7.mixintests.tests.mixins.selectors;

import com.llamalad7.mixintests.ap.annotations.Config;
import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.harness.tests.TestBox;
import com.llamalad7.mixintests.tests.targets.LambdaTarget;
import org.spongepowered.asm.mixin.FabricUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@MixinTest(box = LambdaTarget.class)
public class IgnoredLambdaTargeting {
    @Config(fabricCompat = FabricUtil.COMPATIBILITY_0_17_4)
    @Mixin(LambdaTarget.class)
    static abstract class OldMixin extends TestBox {
        @Inject(method = "runnables ->* *", at = @At("HEAD"))
        private static void rootOnly(CallbackInfo ci) {
            print("\tis the root");
        }

        @Inject(method = "runnables -> {", at = @At("HEAD"))
        private static void rootOnlyInvalidTail(CallbackInfo ci) {
            print("\tis the root");
        }
    }
}
