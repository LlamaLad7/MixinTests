package com.llamalad7.mixintests.tests.mixins.fabric.accessors;

import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.ap.annotations.TestOption;
import com.llamalad7.mixintests.harness.tests.TestBox;
import com.llamalad7.mixintests.tests.targets.AccessorTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@MixinTest(box = AccessorTarget.class, minFabricMixin = "0.17.4", fabricMixin = TestOption.ON)
public class AccessorTest {
    @Mixin(AccessorTarget.class)
    static abstract class Mixin0 extends TestBox {
        @Accessor
        abstract String getTest();

        @Accessor
        abstract String getTEST();

        @Inject(method = "box", at = @At("HEAD"))
        private void box(CallbackInfo ci) {
            print("test: " + getTest());
            print("TEST: " + getTEST());
        }
    }

    @Mixin(AccessorTarget.class)
    static abstract class Mixin1 extends TestBox {
        @Accessor("x")
        abstract String getX();

        @Inject(method = "box", at = @At("HEAD"))
        private void box(CallbackInfo ci) {
            print("x: " + getX());
        }
    }

    @Mixin(AccessorTarget.class)
    static abstract class Mixin2 extends TestBox {
        @Accessor
        abstract String getX();

        @Inject(method = "box", at = @At("HEAD"))
        private void box(CallbackInfo ci) {
            print("x: " + getX());
        }
    }
}
