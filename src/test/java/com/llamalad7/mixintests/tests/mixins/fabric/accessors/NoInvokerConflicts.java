package com.llamalad7.mixintests.tests.mixins.fabric.accessors;

import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.ap.annotations.TestOption;
import com.llamalad7.mixintests.harness.tests.TestBox;
import com.llamalad7.mixintests.tests.targets.InvokerTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@MixinTest(box = InvokerTarget.class, minFabricMixin = "0.17.4", fabricMixin = TestOption.ON)
public class NoInvokerConflicts {
    @Mixin(InvokerTarget.class)
    static abstract class Mixin1 extends TestBox {
        @Invoker("x")
        abstract String callX();

        @Inject(method = "box", at = @At("HEAD"))
        private void box(CallbackInfo ci) {
            print("x: " + callX());
        }
    }

    @Mixin(InvokerTarget.class)
    static abstract class Mixin2 extends TestBox {
        @Invoker
        abstract String callX();

        @Inject(method = "box", at = @At("HEAD"))
        private void box(CallbackInfo ci) {
            print("x: " + callX());
        }
    }
}
