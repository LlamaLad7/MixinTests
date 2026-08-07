package com.llamalad7.mixintests.tests.mixins.fabric.accessors;

import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.ap.annotations.TestOption;
import com.llamalad7.mixintests.harness.tests.TestBox;
import com.llamalad7.mixintests.tests.AccessorUtil;
import com.llamalad7.mixintests.tests.targets.AccessorTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sun.jvm.hotspot.utilities.AssertionFailure;

import java.lang.reflect.Method;

@MixinTest(box = AccessorTarget.class, fabricMixin = TestOption.ON, minFabricMixin = "0.17.4")
public class VisibleAccessors {
    @Mixin(AccessorTarget.class)
    static abstract class AccessorMixin extends TestBox {
        @Accessor("x")
        abstract String getX();

        @Accessor("test")
        public abstract String getTest();

        @Accessor("y")
        abstract String getY();

        @Accessor("z")
        static String getZ() {
            throw new AssertionFailure("Implemented via Mixin");
        }

        @Inject(method = "box", at = @At("HEAD"))
        private void box(CallbackInfo ci) {
            for (Method method : AccessorUtil.getMethods(AccessorMixin.class, "getX", "getTest", "getY")) {
                print(AccessorUtil.formatMethod(method));
            }

            print(AccessorUtil.formatMethod(AccessorUtil.findUniqueMethod(AccessorMixin.class, "getZ")));
        }
    }
}
