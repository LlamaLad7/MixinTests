package com.llamalad7.mixintests.tests.mixins.overwrite;

import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.tests.targets.ExampleTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@MixinTest(box = ExampleTarget.class)
public class OverwriteMethod {
    @Mixin(ExampleTarget.class)
    static class Overwriter {
        @Overwrite
        public String box() {
            return "custom text";
        }
    }
}
