package com.llamalad7.mixintests.tests.mixins.fabric.selectors;

import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.ap.annotations.TestOption;
import com.llamalad7.mixintests.harness.tests.TestBox;
import com.llamalad7.mixintests.tests.targets.LambdaTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@MixinTest(box = LambdaTarget.class, shouldFail = true, fabricMixin = TestOption.ON, minFabricMixin = "0.17.5")
public class LambdaTargetingInvalidNext {
    @Mixin(LambdaTarget.class)
    static abstract class TheMixin extends TestBox {
        @Inject(method = "runnables -> {", at = @At("HEAD"))
        private static void allLambdasPlusRoot(CallbackInfo ci) {
            print("\tis a lambda or the root");
        }
    }
}
