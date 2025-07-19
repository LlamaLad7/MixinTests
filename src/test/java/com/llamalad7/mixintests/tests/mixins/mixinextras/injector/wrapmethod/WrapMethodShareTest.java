package com.llamalad7.mixintests.tests.mixins.mixinextras.injector.wrapmethod;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.ap.annotations.TestOption;
import com.llamalad7.mixintests.harness.tests.TestBox;
import com.llamalad7.mixintests.tests.targets.ExampleVoidTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@MixinTest(box = ExampleVoidTarget.class, mixinExtras = TestOption.ON, minMixinExtras = "0.4.0")
public class WrapMethodShareTest {
    private static final String INT = "exampleInt";
    private static final String STRING = "exampleString";

    @Mixin(ExampleVoidTarget.class)
    static abstract class TheMixin extends TestBox {
        @WrapMethod(method = "box")
        private void wrapInner(Operation<Void> original, @Share(INT) LocalIntRef exampleInt, @Share(STRING) LocalRef<String> exampleString) {
            print("Inner start: " + exampleInt.get() + " " + exampleString.get());
            exampleInt.set(2394);
            exampleString.set("innerString");
            original.call();
        }

        @WrapMethod(method = "box")
        private void wrapOuter(Operation<Void> original, @Share(STRING) LocalRef<String> exampleString, @Share(INT) LocalIntRef exampleInt) {
            exampleInt.set(-123);
            exampleString.set("outerString");
            original.call();
            print("Outer end: " + exampleInt.get() + " " + exampleString.get());
        }

        @Inject(method = "box", at = @At("HEAD"))
        private void headInject(CallbackInfo ci, @Share(STRING) LocalRef<String> exampleString) {
            print("Got headInject: " + exampleString.get());
            exampleString.set("injectString");
        }

        @Inject(method = "box", at = @At("RETURN"))
        private void returnInject(CallbackInfo ci, @Share(INT) LocalIntRef exampleInt) {
            print("Got returnInject: " + exampleInt.get());
            exampleInt.set(12345678);
        }
    }
}
