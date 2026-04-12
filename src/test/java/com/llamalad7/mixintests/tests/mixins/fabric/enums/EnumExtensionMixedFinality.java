package com.llamalad7.mixintests.tests.mixins.fabric.enums;

import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.ap.annotations.TestOption;
import com.llamalad7.mixintests.tests.targets.EnumTargetMixedFinality;
import org.spongepowered.asm.mixin.Mixin;

@MixinTest(box = EnumTargetMixedFinality.class, fabricMixin = TestOption.ON, minFabricMixin = "0.17.2")
public class EnumExtensionMixedFinality {
    @Mixin(EnumTargetMixedFinality.NonFinalEnum.class)
    enum NonFinalTargetFinalMixin {
        D
    }

    @Mixin(EnumTargetMixedFinality.FinalEnum.class)
    enum FinalTargetNonFinalMixin {
        D {
            @Override
            public String toString() {
                return "D overridden";
            }
        }
    }
}
