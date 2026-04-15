package com.llamalad7.mixintests.tests.mixins.fabric.merging;

import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.ap.annotations.TestOption;
import com.llamalad7.mixintests.harness.tests.TestBox;
import com.llamalad7.mixintests.tests.targets.ExampleVoidTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;

@MixinTest(box = ExampleVoidTarget.class, fabricMixin = TestOption.ON, minFabricMixin = "0.17.1")
public class PolymorphicSignatureTest {
    @Mixin(ExampleVoidTarget.class)
    static abstract class TheMixin extends TestBox {
        private static final MethodHandle DO_THING;
        private static final VarHandle TEST_FIELD;

        private final String testField = "mixin works";

        @Inject(method = "box", at = @At("HEAD"))
        private void onBox(CallbackInfo ci) throws Throwable {
            DO_THING.invokeExact(this);
        }

        public void doThing() {
            print(TEST_FIELD.get(this));
        }

        static {
            try {
                DO_THING = MethodHandles.lookup().findVirtual(TheMixin.class, "doThing", MethodType.methodType(void.class));
                TEST_FIELD = MethodHandles.lookup().findVarHandle(TheMixin.class, "testField", String.class);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
