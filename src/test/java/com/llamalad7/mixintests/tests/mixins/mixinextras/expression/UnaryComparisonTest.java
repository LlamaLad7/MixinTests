package com.llamalad7.mixintests.tests.mixins.mixinextras.expression;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.ap.annotations.TestOption;
import com.llamalad7.mixintests.harness.tests.TestBox;
import com.llamalad7.mixintests.tests.targets.UnaryComparisonTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@MixinTest(box = UnaryComparisonTarget.class, mixinExtras = TestOption.ON, minMixinExtras = "0.5.0-beta.1")
public class UnaryComparisonTest {
    @Mixin(UnaryComparisonTarget.class)
    static abstract class TheMixin extends TestBox {
        @ModifyConstant(method = "box", constant = @Constant(expandZeroConditions = Constant.Condition.GREATER_THAN_ZERO))
        private int change0To10(int zero) {
            print("Expected 0 got " + zero);
            return 10;
        }

        @Definition(id = "number", field = "Lcom/llamalad7/mixintests/tests/targets/UnaryComparisonTarget;number:I")
        @Expression("this.number > @(0)")
        @ModifyExpressionValue(method = "box", at = @At("MIXINEXTRAS:EXPRESSION"))
        private int add15To10(int ten) {
            print("Expected 10 got " + ten);
            return ten + 15;
        }

        @Definition(id = "number", field = "Lcom/llamalad7/mixintests/tests/targets/UnaryComparisonTarget;number:I")
        @Expression("this.number > 0")
        @WrapOperation(method = "box", at = @At("MIXINEXTRAS:EXPRESSION"))
        private boolean wrapComparison(int left, int right, Operation<Boolean> original) {
            print("Doing " + left + " > " + right);
            boolean result = original.call(left, right);
            print("Was " + result);
            return !result;
        }
    }
}
