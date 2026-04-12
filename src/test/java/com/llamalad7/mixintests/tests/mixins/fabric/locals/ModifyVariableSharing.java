package com.llamalad7.mixintests.tests.mixins.fabric.locals;

import com.llamalad7.mixintests.ap.annotations.FabricCompat;
import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.ap.annotations.TestOption;
import com.llamalad7.mixintests.tests.targets.ParameterTarget;
import org.spongepowered.asm.mixin.FabricUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@MixinTest(box = ParameterTarget.class, fabricMixin = TestOption.ON, minFabricMixin = "0.17.2")
public class ModifyVariableSharing {
    @FabricCompat(FabricUtil.COMPATIBILITY_0_16_5)
    @Mixin(value = ParameterTarget.class, priority = 500)
    static class OldMixin {
        @ModifyVariable(method = "doThing", at = @At("HEAD"), argsOnly = true, name = "arg2")
        private String modifyString(String original) {
            return original + " (modified by old)";
        }
    }

    @Mixin(ParameterTarget.class)
    static class NewMixin {
        @ModifyVariable(method = "doThing", at = @At("HEAD"), argsOnly = true, name = "string")
        private String modifyString(String original) {
            return original + " (modified by new)";
        }
    }
}
