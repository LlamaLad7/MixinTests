package com.llamalad7.mixintests.tests.mixins.mixinextras.injector.modifyexpressionvalue;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.ap.annotations.TestOption;
import com.llamalad7.mixintests.harness.tests.TestBox;
import com.llamalad7.mixintests.tests.targets.ExampleStaticTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Redirect;

@MixinTest(box = ExampleStaticTarget.class, mixinExtras = TestOption.ON, minMixinExtras = "0.5.0-rc.1")
public class ModifyExpressionValueCoerceTest {
    @Mixin(ExampleStaticTarget.class)
    static abstract class TheMixin extends TestBox {
        @Shadow
        private static String staticBox() {
            throw new AssertionError();
        }

        @ModifyExpressionValue(method = "staticBox", at = @At(value = "CONSTANT", args = "stringValue=hello"))
        private static @Coerce CharSequence coerceOriginal(@Coerce CharSequence original) {
            return "coerceOriginal(" + original + ")";
        }

        @Redirect(method = "box", at = @At(value = "INVOKE", target = "Lcom/llamalad7/mixintests/tests/targets/ExampleStaticTarget;staticBox()Ljava/lang/String;"))
        private @Coerce CharSequence redirect() {
            return "redirect(" + staticBox() + ")";
        }

        @ModifyExpressionValue(method = "box", at = @At(value = "INVOKE", target = "Lcom/llamalad7/mixintests/tests/targets/ExampleStaticTarget;staticBox()Ljava/lang/String;"))
        private String normalMev(String original) {
            return "normalMev(" + original + ")";
        }

        @ModifyExpressionValue(method = "box", at = @At(value = "INVOKE", target = "Lcom/llamalad7/mixintests/tests/targets/ExampleStaticTarget;staticBox()Ljava/lang/String;"))
        private String afterRedirect(String original) {
            return "afterRedirect(" + original + ")";
        }
    }
}
