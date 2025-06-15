package com.llamalad7.mixintests.tests.mixins.fabric.injector.inject;

import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.ap.annotations.TestOption;
import com.llamalad7.mixintests.tests.targets.ExampleVoidTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@MixinTest(box = ExampleVoidTarget.class, fabricMixin = TestOption.ON, testBytecode = true)
public class NullPassedForUnusedCallbackInfo {
    @Mixin(ExampleVoidTarget.class)
    static class TheMixin {
        @Inject(method = "box", at = @At("RETURN"))
        private void test(CallbackInfoReturnable<String> cir) {
        }

        @Inject(method = "modify", at = @At("RETURN"))
        private void test(StringBuilder builder, CallbackInfo ci) {
            builder.append(" modified");
        }
    }
}
