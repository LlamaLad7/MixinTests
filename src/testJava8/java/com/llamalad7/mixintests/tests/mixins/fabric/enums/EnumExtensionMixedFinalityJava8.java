package com.llamalad7.mixintests.tests.mixins.fabric.enums;

import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.ap.annotations.TestOption;
import com.llamalad7.mixintests.tests.targets.java8.Java8EnumTargetMixedFinality;
import org.spongepowered.asm.mixin.Mixin;

@MixinTest(box = Java8EnumTargetMixedFinality.class, fabricMixin = TestOption.ON, minFabricMixin = "0.17.2")
public class EnumExtensionMixedFinalityJava8 {
    @Mixin(Java8EnumTargetMixedFinality.NonFinalEnum.class)
    enum NonFinalTargetFinalMixin {
        D
    }

    @Mixin(Java8EnumTargetMixedFinality.FinalEnum.class)
    enum FinalTargetNonFinalMixin {
        D {
            @Override
            public String toString() {
                return "D overridden";
            }
        }
    }
}
