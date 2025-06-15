package com.llamalad7.mixintests.tests.mixins.injector.modifyargs;

import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.tests.targets.ExampleTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@MixinTest(box = ExampleTarget.class)
public class ModifyArgsOnRedirect {
    @Mixin(ExampleTarget.class)
    static class Redirector {
        @Redirect(method = "box", at = @At(value = "INVOKE", target = "Ljava/lang/String;repeat(I)Ljava/lang/String;"))
        private String test(String instance, int i) {
            return "goodbye".repeat(i);
        }
    }

    @Mixin(value = ExampleTarget.class, priority = 1500)
    static class LateMixin {
        @ModifyArgs(method = "box", at = @At(value = "INVOKE", target = "Ljava/lang/String;repeat(I)Ljava/lang/String;"))
        private void test(Args args) {
            args.set(0, 3);
        }
    }
}
