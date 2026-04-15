package com.llamalad7.mixintests.tests.mixins.mixinextras.sugar.locals;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.ap.annotations.TestOption;
import com.llamalad7.mixintests.tests.targets.ParameterTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@MixinTest(box = ParameterTarget.class, mixinExtras = TestOption.ON)
public class BasicLocalUsage {
    @Mixin(ParameterTarget.class)
    static abstract class TheMixin {
        @Inject(method = "doThing", at = @At("HEAD"))
        private void test(CallbackInfo ci, @Local(argsOnly = true) LocalIntRef numberRef) {
            numberRef.set(numberRef.get() + 1);
        }
    }
}
