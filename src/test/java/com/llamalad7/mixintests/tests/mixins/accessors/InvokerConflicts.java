package com.llamalad7.mixintests.tests.mixins.accessors;

import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.tests.targets.InvokerTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@MixinTest(box = InvokerTarget.class, shouldFail = true)
public class InvokerConflicts {
    @Mixin(InvokerTarget.class)
    interface Mixin1 {
        @Invoker("x")
        String sameName();
    }

    @Mixin(InvokerTarget.class)
    interface Mixin2 {
        @Invoker("test")
        String sameName();
    }
}

