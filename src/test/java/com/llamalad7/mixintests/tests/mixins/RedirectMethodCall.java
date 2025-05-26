package com.llamalad7.mixintests.tests.mixins;

import com.llamalad7.mixintests.ap.annotations.MixinTestGroup;
import com.llamalad7.mixintests.tests.targets.ExampleTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@MixinTestGroup
public class RedirectMethodCall {
    @Mixin(ExampleTarget.class)
    static class Redirector {
        @Redirect(method = "getEleven", at = @At(value = "INVOKE", target = "Ljava/lang/Math;floor(D)D"))
        private static double test(double a) {
            return 100;
        }
    }

    @Mixin(value = ExampleTarget.class, priority = 1500)
    static class LateMixin {
        @ModifyArg(method = "getEleven", at = @At(value = "INVOKE", target = "Ljava/lang/Math;floor(D)D"))
        private static double test(double a) {
            return 1000;
        }
    }
}
