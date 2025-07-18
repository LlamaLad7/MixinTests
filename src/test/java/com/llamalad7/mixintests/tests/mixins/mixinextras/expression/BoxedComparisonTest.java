package com.llamalad7.mixintests.tests.mixins.mixinextras.expression;

import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.ap.annotations.TestOption;
import com.llamalad7.mixintests.harness.tests.TestBox;
import com.llamalad7.mixintests.tests.targets.BoxedComparisonTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@MixinTest(box = BoxedComparisonTarget.class, mixinExtras = TestOption.ON, minMixinExtras = "0.5.0-beta.1")
public class BoxedComparisonTest {
    @Mixin(BoxedComparisonTarget.class)
    static abstract class TheMixin extends TestBox {
        @Expression("10 > ?")
        @WrapOperation(method = "box", at = @At("MIXINEXTRAS:EXPRESSION"))
        private boolean test(int left, int right, Operation<Boolean> original) {
            print("Doing " + left + " > " + right);
            return !original.call(left, right);
        }
    }
}
