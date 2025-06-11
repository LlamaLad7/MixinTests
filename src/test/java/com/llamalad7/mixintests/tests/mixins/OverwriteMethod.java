package com.llamalad7.mixintests.tests.mixins;

import com.llamalad7.mixintests.ap.annotations.MixinTestGroup;
import com.llamalad7.mixintests.tests.targets.ExampleTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@MixinTestGroup
public class OverwriteMethod {
    @Mixin(ExampleTarget.class)
    static class Overwriter {
        @Overwrite
        public static int getEleven() {
            return 5;
        }
    }
}
