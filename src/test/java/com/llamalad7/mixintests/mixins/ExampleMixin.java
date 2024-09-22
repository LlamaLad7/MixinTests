package com.llamalad7.mixintests.mixins;

import com.llamalad7.mixintests.tests.targets.ExampleTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ExampleTarget.class)
public class ExampleMixin {
    @Inject(method = "getEleven", at = @At("HEAD"), cancellable = true)
    private static void test(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(11);
    }
}
