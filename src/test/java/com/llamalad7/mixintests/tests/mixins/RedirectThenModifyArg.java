package com.llamalad7.mixintests.tests.mixins;

import com.llamalad7.mixintests.ap.annotations.MixinTestGroup;
import com.llamalad7.mixintests.tests.targets.ExampleTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@MixinTestGroup(box = ExampleTarget.class, minMixin = "0.8.6", minFabricMixin = "0.13.0+mixin.0.8.5")
public class RedirectThenModifyArg {
    @Mixin(ExampleTarget.class)
    static class Redirector {
        @Redirect(method = "box", at = @At(value = "INVOKE", target = "Ljava/lang/String;repeat(I)Ljava/lang/String;"))
        private String test(String instance, int i) {
            return "goodbye".repeat(i);
        }
    }

    @Mixin(value = ExampleTarget.class, priority = 1500)
    static class LateMixin {
        @ModifyArg(method = "box", at = @At(value = "INVOKE", target = "Ljava/lang/String;repeat(I)Ljava/lang/String;"), index = 0)
        private int test(int count) {
            return 3;
        }
    }
}
