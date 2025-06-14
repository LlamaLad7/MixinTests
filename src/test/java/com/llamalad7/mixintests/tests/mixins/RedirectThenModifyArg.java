package com.llamalad7.mixintests.tests.mixins;

import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.tests.targets.ExampleTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@MixinTest(box = ExampleTarget.class)
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
