package com.llamalad7.mixintests.tests.mixins.mixinextras.expression;

import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.ap.annotations.TestOption;
import com.llamalad7.mixintests.tests.targets.IntComparisonTarget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@MixinTest(box = IntComparisonTarget.class, mixinExtras = TestOption.ON, minMixinExtras = "0.5.0-beta.1")
public class IntLikeTest {
    @Mixin(IntComparisonTarget.class)
    static class TheMixin {
        @Shadow @Final private StringBuilder builder;

        @Expression("65")
        @ModifyExpressionValue(method = "box", at = @At("MIXINEXTRAS:EXPRESSION"))
        private int modifyInt(int original) {
            return original + 1;
        }

        @Expression("'A'")
        @ModifyExpressionValue(method = "box", at = @At("MIXINEXTRAS:EXPRESSION"))
        private char modifyChar(char original) {
            addLine("Expected 'A' got '" + original + '\'');
            return 'B';
        }

        @Expression("? == 66")
        @WrapOperation(method = "box", at = @At("MIXINEXTRAS:EXPRESSION"))
        private boolean wrapComparisonInner(int left, int right, Operation<Boolean> original) {
            addLine("Doing " + left + " == " + right);
            return original.call(left, right);
        }

        @Expression("? == 'B'")
        @WrapOperation(method = "box", at = @At("MIXINEXTRAS:EXPRESSION"))
        private boolean wrapComparisonOuter(char left, char right, Operation<Boolean> original) {
            addLine("Doing " + left + " == 'C'");
            return original.call(left, 'C');
        }

        @Unique
        private void addLine(String line) {
            builder.append(line).append('\n');
        }
    }
}
