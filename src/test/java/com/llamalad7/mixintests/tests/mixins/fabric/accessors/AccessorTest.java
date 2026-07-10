package com.llamalad7.mixintests.tests.mixins.fabric.accessors;

import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.ap.annotations.TestOption;
import com.llamalad7.mixintests.tests.targets.AccessorTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@MixinTest(box = AccessorTarget.class, fabricMixin = TestOption.ON)
public class AccessorTest {
    @Mixin(AccessorTarget.class)
    interface Mixin0 {
        @Accessor
        String getTest();

        @Accessor
        String getTEST();
    }

    @Mixin(AccessorTarget.class)
    interface Mixin1 {
        @Accessor("x")
        String getX();
    }

    @Mixin(AccessorTarget.class)
    interface Mixin2 {
        @Accessor
        String getX();
    }
}
