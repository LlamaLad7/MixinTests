package com.llamalad7.mixintests.tests.mixins.accessors;

import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.tests.targets.AccessorTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@MixinTest(box = AccessorTarget.class, shouldFail = true)
public class AccessorConflicts {
    @Mixin(AccessorTarget.class)
    interface Mixin1 {
        @Accessor("x")
        String sameName();
    }

    @Mixin(AccessorTarget.class)
    interface Mixin2 {
        @Accessor("test")
        String sameName();
    }
}
