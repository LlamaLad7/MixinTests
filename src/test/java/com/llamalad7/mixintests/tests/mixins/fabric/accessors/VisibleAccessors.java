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

import java.lang.reflect.Method;

@MixinTest(box = AccessorTarget.class, fabricMixin = TestOption.ON, minFabricMixin = "0.17.4")
public class VisibleAccessors {
    @Mixin(AccessorTarget.class)
    static abstract class Mixin1 extends TestBox {
        @Accessor("x")
        abstract String getX();

        @Accessor("test")
        public abstract String getTest();

        @Inject(method = "box", at = @At("HEAD"))
        private void box(CallbackInfo ci) throws NoSuchMethodException {
            Method[] methods = new Method[] {
                    Mixin1.class.getDeclaredMethod("getX"),
                    Mixin1.class.getDeclaredMethod("getTest")
            };

            for (Method method : methods) {
                print(method + ": " + String.format("%04x", method.getModifiers()));
            }
        }
    }
}
