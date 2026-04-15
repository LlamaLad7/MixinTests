package com.llamalad7.mixintests.tests.mixins.injector.inject;

import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.tests.targets.TargetWithLocalVariables;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@MixinTest(box = TargetWithLocalVariables.class, minFabricMixin = "0.17.2")
public class CallbackInfoNotNull {
    @Mixin(TargetWithLocalVariables.class)
    static abstract class TheMixin {
        @Shadow @Final private static StringBuilder builder;

        //region with param
        @Inject(method = "doThing", at = @At(value = "RETURN", id = "nonStaticIntoNonStatic"))
        private void nonStaticIntoNonStatic(StringBuilder builder, CallbackInfo ci) {
            TheMixin.builder.append(ci.getId()).append('\n');
        }

        @Inject(method = "doThing", at = @At(value = "RETURN", id = "staticIntoNonStatic"))
        private static void staticIntoNonStatic(StringBuilder builder, CallbackInfo ci) {
            TheMixin.builder.append(ci.getId()).append('\n');
        }

        @Inject(method = "staticDoThing", at = @At(value = "RETURN", id = "staticIntoStatic"))
        private static void staticIntoStatic(StringBuilder builder, CallbackInfo ci) {
            TheMixin.builder.append(ci.getId()).append('\n');
        }
        //endregion

        //region with no param
        @Inject(method = "doThing", at = @At(value = "RETURN", id = "nonStaticIntoNonStaticNoParam"))
        private void nonStaticIntoNonStaticNoParam(CallbackInfo ci) {
            TheMixin.builder.append(ci.getId()).append('\n');
        }

        @Inject(method = "doThing", at = @At(value = "RETURN", id = "staticIntoNonStaticNoParam"))
        private static void staticIntoNonStaticNoParam(CallbackInfo ci) {
            TheMixin.builder.append(ci.getId()).append('\n');
        }

        @Inject(method = "staticDoThing", at = @At(value = "RETURN", id = "staticIntoStaticNoParam"))
        private static void staticIntoStaticNoParam(CallbackInfo ci) {
            TheMixin.builder.append(ci.getId()).append('\n');
        }
        //endregion

        //region with locals
        @Inject(method = "doThing", at = @At(value = "RETURN", id = "nonStaticIntoNonStaticWithLocals"), locals = LocalCapture.CAPTURE_FAILHARD)
        private void nonStaticIntoNonStaticWithLocals(StringBuilder builder, CallbackInfo ci, int a, int b) {
            TheMixin.builder.append(ci.getId()).append('\n');
        }

        @Inject(method = "doThing", at = @At(value = "RETURN", id = "staticIntoNonStaticWithLocals"), locals = LocalCapture.CAPTURE_FAILHARD)
        private static void staticIntoNonStaticWithLocals(StringBuilder builder, CallbackInfo ci, int a, int b) {
            TheMixin.builder.append(ci.getId()).append('\n');
        }

        @Inject(method = "staticDoThing", at = @At(value = "RETURN", id = "staticIntoStaticWithLocals"), locals = LocalCapture.CAPTURE_FAILHARD)
        private static void staticIntoStaticWithLocals(StringBuilder builder, CallbackInfo ci, int a, int b) {
            TheMixin.builder.append(ci.getId()).append('\n');
        }
        //endregion
    }
}
