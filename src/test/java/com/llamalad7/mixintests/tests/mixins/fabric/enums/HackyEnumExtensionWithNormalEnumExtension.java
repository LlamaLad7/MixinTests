package com.llamalad7.mixintests.tests.mixins.fabric.enums;

import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.ap.annotations.TestOption;
import com.llamalad7.mixintests.harness.tests.TestBox;
import com.llamalad7.mixintests.tests.targets.BasicEnumTarget;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

@MixinTest(box = BasicEnumTarget.class, fabricMixin = TestOption.ON)
public class HackyEnumExtensionWithNormalEnumExtension {
    @Mixin(value = BasicEnumTarget.BasicEnum.class, priority = 500)
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
            TestBox.print("hacky extension <clinit>");
            BasicEnumTarget.BasicEnum entry = create("NEW_ENTRY", $VALUES.length);
            $VALUES = ArrayUtils.add($VALUES, entry);
        }
    }

    @Mixin(BasicEnumTarget.BasicEnum.class)
    enum NormalEnumExtensionMixin {
        NEW_ENTRY_2;

        static {
            TestBox.print("normal extension <clinit>");
        }
    }
}
