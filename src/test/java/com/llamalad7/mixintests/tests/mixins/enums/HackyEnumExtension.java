package com.llamalad7.mixintests.tests.mixins.enums;

import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.harness.tests.TestBox;
import com.llamalad7.mixintests.tests.targets.BasicEnumTarget;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

@MixinTest(box = BasicEnumTarget.class, minFabricMixin = "0.17.3")
public class HackyEnumExtension {
    @Mixin(BasicEnumTarget.BasicEnum.class)
    static class HackyEnumExtensionMixin {
        @Shadow
        @Final
        @Mutable
        private static BasicEnumTarget.BasicEnum[] $VALUES;

        @Invoker(value="<init>")
        private static BasicEnumTarget.BasicEnum create(String name, int ordinal) {
            throw new IllegalStateException("Unreachable");
        }

        static {
            TestBox.print("hacky <clinit>");
            BasicEnumTarget.BasicEnum entry = create("NEW_ENTRY", $VALUES.length);
            $VALUES = ArrayUtils.add($VALUES, entry);
        }
    }
}
