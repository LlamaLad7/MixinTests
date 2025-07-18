package com.llamalad7.mixintests.tests.mixins.mixinextras.expression;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.ap.annotations.TestOption;
import com.llamalad7.mixintests.tests.targets.UnaryComparisonTarget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@MixinTest(box = UnaryComparisonTarget.class, mixinExtras = TestOption.ON, minMixinExtras = "0.5.0-beta.1")
public class UnaryComparisonTest {
    @Mixin(UnaryComparisonTarget.class)
    static class TheMixin {
        @Shadow @Final private StringBuilder builder;

        @ModifyConstant(method = "box", constant = @Constant(expandZeroConditions = Constant.Condition.GREATER_THAN_ZERO))
        private int change0To10(int zero) {
            addLine("Expected 0 got " + zero);
            return 10;
        }

        @Definition(id = "number", field = "Lcom/llamalad7/mixintests/tests/targets/UnaryComparisonTarget;number:I")
        @Expression("this.number > @(0)")
        @ModifyExpressionValue(method = "box", at = @At("MIXINEXTRAS:EXPRESSION"))
        private int add15To10(int ten) {
            addLine("Expected 10 got " + ten);
            return ten + 15;
        }

        @Definition(id = "number", field = "Lcom/llamalad7/mixintests/tests/targets/UnaryComparisonTarget;number:I")
        @Expression("this.number > 0")
        @WrapOperation(method = "box", at = @At("MIXINEXTRAS:EXPRESSION"))
        private boolean wrapComparison(int left, int right, Operation<Boolean> original) {
            addLine("Doing " + left + " > " + right);
            boolean result = original.call(left, right);
            addLine("Was " + result);
            return !result;
        }

        @Unique
        private void addLine(String line) {
            builder.append(line).append('\n');
        }
    }
}
