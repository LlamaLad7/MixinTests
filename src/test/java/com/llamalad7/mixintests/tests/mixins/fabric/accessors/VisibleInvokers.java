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

import java.lang.reflect.Method;

@MixinTest(box = InvokerTarget.class, fabricMixin = TestOption.ON, minFabricMixin = "0.17.4")
public class VisibleInvokers {
    @Mixin(InvokerTarget.class)
    static abstract class InvokerMixin extends TestBox {
        @Invoker("x")
        abstract String callX();

        @Invoker("test")
        public abstract String callTest();

        @Inject(method = "box", at = @At("HEAD"))
        private void box(CallbackInfo ci) throws NoSuchMethodException {
            for (Method method : AccessorUtil.getMethods(InvokerMixin.class, "callX", "callTest")) {
                print(AccessorUtil.formatMethod(method));
            }
        }
    }
}
