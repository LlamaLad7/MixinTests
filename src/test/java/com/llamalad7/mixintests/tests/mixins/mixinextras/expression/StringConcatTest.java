package com.llamalad7.mixintests.tests.mixins.mixinextras.expression;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.ap.annotations.TestOption;
import com.llamalad7.mixintests.tests.targets.StringConcatTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@MixinTest(box = StringConcatTarget.class, mixinExtras = TestOption.ON, minMixinExtras = "0.5.0-rc.4")
public class StringConcatTest {
    @Mixin(StringConcatTarget.class)
    static class TheMixin {
        @Definition(id = "number", field = "Lcom/llamalad7/mixintests/tests/targets/StringConcatTarget;number:I")
        @Expression("this.number + ' is a number\n'")
        @ModifyExpressionValue(method = "box", at = @At("MIXINEXTRAS:EXPRESSION"))
        private String wholeTwoPart(String original) {
            return "v 2 parts\n" + original + "^ 2 parts\n\n";
        }

        @Definition(id = "doubleNumber", field = "Lcom/llamalad7/mixintests/tests/targets/StringConcatTarget;doubleNumber:D")
        @Expression("'this ' + this.doubleNumber + ' is negative\n'")
        @ModifyExpressionValue(method = "box", at = @At("MIXINEXTRAS:EXPRESSION"))
        private String wholeThreePart(String original) {
            return "v 3 parts\n" + original + "^ 3 parts\n\n";
        }

        @Expression("@(?) + ' is a number\n'")
        @ModifyExpressionValue(method = "box", at = @At("MIXINEXTRAS:EXPRESSION"))
        private int addToWildcardNumber(int number) {
            return number + 7;
        }

        @Definition(id = "number", field = "Lcom/llamalad7/mixintests/tests/targets/StringConcatTarget;number:I")
        @Expression("@(this.number) + ' is a number\n'")
        @ModifyExpressionValue(method = "box", at = @At("MIXINEXTRAS:EXPRESSION"))
        private int tripleNumber(int number) {
            return number * 3;
        }

        @Expression("'this ' + @(?)")
        @ModifyExpressionValue(method = "box", at = @At("MIXINEXTRAS:EXPRESSION"))
        private double halveDouble(double original) {
            return original / 2;
        }

        @Expression("@(?) + ' is negative\n'")
        @ModifyExpressionValue(method = "box", at = @At("MIXINEXTRAS:EXPRESSION"))
        private String modifyWildcardPartialResult(String original) {
            return original + " truly";
        }

        @Definition(id = "doubleNumber", field = "Lcom/llamalad7/mixintests/tests/targets/StringConcatTarget;doubleNumber:D")
        @Expression("'this ' + this.doubleNumber")
        @ModifyExpressionValue(method = "box", at = @At("MIXINEXTRAS:EXPRESSION"))
        private String modifyPartialResult(String original) {
            return original + " really";
        }

        @Definition(id = "builder", field = "Lcom/llamalad7/mixintests/tests/targets/StringConcatTarget;builder:Ljava/lang/StringBuilder;")
        @Definition(id = "append", method = "Ljava/lang/StringBuilder;append(Ljava/lang/String;)Ljava/lang/StringBuilder;")
        @Expression("this.builder.append(? + @(?))")
        @ModifyExpressionValue(method = "box", at = @At("MIXINEXTRAS:EXPRESSION"))
        private String modifyLastComponent(String original) {
            return original.replace("is ", "is definitely ");
        }
    }
}
