package com.llamalad7.mixintests.tests.mixins.mixinextras.fabric.locals;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.llamalad7.mixintests.ap.annotations.FabricCompat;
import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.ap.annotations.TestOption;
import com.llamalad7.mixintests.tests.targets.ParameterTarget;
import org.spongepowered.asm.mixin.FabricUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@MixinTest(box = ParameterTarget.class, fabricMixin = TestOption.ON, minFabricMixin = "0.17.2", mixinExtras = TestOption.ON, minMixinExtras = "0.5.4")
public class LocalSharing {
    @FabricCompat(FabricUtil.COMPATIBILITY_0_16_5)
    @Mixin(value = ParameterTarget.class, priority = 500)
    static class OldMixin {
        @Inject(method = "doThing", at = @At("HEAD"))
        private void modifyString(CallbackInfo ci, @Local(argsOnly = true, name = "arg2") LocalRef<String> stringRef) {
            stringRef.set(stringRef.get() + " (modified by old)");
        }
    }

    @Mixin(ParameterTarget.class)
    static class NewMixin {
        @Inject(method = "doThing", at = @At("HEAD"))
        private void modifyString(CallbackInfo ci, @Local(argsOnly = true, name = "string") LocalRef<String> stringRef) {
            stringRef.set(stringRef.get() + " (modified by new)");
        }
    }
}
