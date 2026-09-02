package com.llamalad7.mixintests.tests.mixins.selectors;

import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.harness.tests.TestBox;
import com.llamalad7.mixintests.tests.targets.EmptyTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@MixinTest(box = EmptyTarget.class)
public class DuplicateSelectors {
    @Mixin(EmptyTarget.class)
    static abstract class TheMixin extends TestBox {
        @Inject(method = {"box", "box()V"}, at = @At("HEAD"))
        private void duplicateMethod(CallbackInfo ci) {
            print("injected at head");
        }

        @Inject(method = "box", at = {@At("RETURN"), @At("TAIL")})
        private void duplicateAt(CallbackInfo ci) {
            print("injected at return");
        }
    }
}
