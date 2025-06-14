package com.llamalad7.mixintests.tests.mixins;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.ap.annotations.TestOption;
import com.llamalad7.mixintests.tests.targets.ExampleStaticTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;

@MixinTest(box = ExampleStaticTarget.class, mixinExtras = TestOption.ON, minMixinExtras = "0.4.1")
public class ModifyReceiverOnStaticRedirect {
    @Mixin(ExampleStaticTarget.class)
    static class EarlyMixin {
        @Redirect(method = "staticBox", at = @At(value = "INVOKE", target = "Ljava/lang/String;repeat(I)Ljava/lang/String;"), order = InjectionInfo.InjectorOrder.DEFAULT)
        private static String test(String instance, int i) {
            return "oops".repeat(i);
        }
    }

    @Mixin(value = ExampleStaticTarget.class, priority = 1500)
    static class LateMixin {
        @ModifyReceiver(method = "staticBox", at = @At(value = "INVOKE", target = "Ljava/lang/String;repeat(I)Ljava/lang/String;"))
        private static String test(String original, int i) {
            return original + "ies";
        }
    }
}
