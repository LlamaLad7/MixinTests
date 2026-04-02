package com.llamalad7.mixintests.tests.mixins.fabric.initialisers;

import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.ap.annotations.TestOption;
import com.llamalad7.mixintests.tests.targets.InitialiserTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@MixinTest(box = InitialiserTarget.class, fabricMixin = TestOption.ON)
public class InitialiserTargetingNew {
    @Mixin(value = InitialiserTarget.class, priority = 1500)
    static class TargetingMixin {
        @ModifyConstant(method = {"<init>", "<clinit>"}, constant = @Constant(stringValue = "merged"), require = 0)
        private static String modifyString(String original) {
            return "modified";
        }
    }

    @Mixin(InitialiserTarget.class)
    static class InitialiserMixin {
        @Shadow private static String STATIC_FIELD = "merged";

        @Shadow private String nonStaticField = "merged";
    }
}
