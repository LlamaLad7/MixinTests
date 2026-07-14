package com.llamalad7.mixintests.tests.mixins.fabric.injector;

import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.ap.annotations.TestOption;
import com.llamalad7.mixintests.tests.targets.ExampleVoidTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@MixinTest(box = ExampleVoidTarget.class, fabricMixin = TestOption.ON, minFabricMixin = "0.17.4", mixinOptions = "REFMAP_REMAP=true", shouldFail = true)
public class PermissiveTargetSelectors {
    @Mixin(ExampleVoidTarget.class)
    static class TheMixin {
        @Inject(method = "box(LRandom;)LNonsense;", at = @At("HEAD"))
        private static void test(CallbackInfo ci) {
        }
    }
}
