package com.llamalad7.mixintests.tests.mixins.fabric.selectors;

import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.ap.annotations.TestOption;
import com.llamalad7.mixintests.harness.tests.TestBox;
import com.llamalad7.mixintests.tests.targets.LambdaTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@MixinTest(box = LambdaTarget.class, fabricMixin = TestOption.ON, minFabricMixin = "0.17.5")
public class LambdaTargeting {
    @Mixin(LambdaTarget.class)
    static abstract class TheMixin extends TestBox {
        @Inject(method = "runnables ->* *", at = @At("HEAD"))
        private static void allLambdasPlusRoot(CallbackInfo ci) {
            print("\tis a lambda or the root");
        }

        @Inject(method = {"runnables", "runnables ->+ *"}, at = @At("HEAD"))
        private static void allLambdasPlusRoot2(CallbackInfo ci) {
            print("\tis a lambda or the root");
        }

        @Inject(method = "runnables ->+ *", at = @At("HEAD"))
        private static void allLambdasWithoutRoot(CallbackInfo ci) {
            print("\tis a lambda");
        }

        @Inject(method = "runnables -> *", at = @At("HEAD"))
        private static void topLevelLambdas(CallbackInfo ci) {
            print("\tis a top-level lambda");
        }

        @Inject(method = "runnables -> *()V", at = @At("HEAD"))
        private static void topLevelLambdasWithDesc(CallbackInfo ci) {
            print("\tis a top-level lambda with desc ()V");
        }

        @Inject(method = "runnables ->* ()V", at = @At("HEAD"))
        private static void lambdasWithDesc(CallbackInfo ci) {
            print("\tis a lambda or the root with desc ()V");
        }

        @Inject(method = "runnables ->* *()V", at = @At("HEAD"))
        private static void allLambdasWithDesc(CallbackInfo ci) {
            print("\tis a lambda or the root with desc ()V");
        }

        @Inject(method = "runnables ->{2,3} *", at = @At("HEAD"))
        private static void doublyOrTriplyNestedLambdas(CallbackInfo ci) {
            print("\thas depth 2-3");
        }

        @Inject(method = "runnables ->* Ljava/lang/Runnable;*", at = @At("HEAD"))
        private static void allLambdasWithOwner(CallbackInfo ci) {
            print("\tis a Runnable");
        }

        @Inject(method = "runnables ->* run*", at = @At("HEAD"))
        private static void allLambdasWithName(CallbackInfo ci) {
            print("\tis named run");
        }

        @Inject(method = "runnables ->* Ljava/lang/Runnable;run()V", at = @At("HEAD"))
        private static void allLambdasWithFull(CallbackInfo ci) {
            print("\tis called Ljava/lang/Runnable;run()V");
        }

        @Inject(method = "runnables ->* {3}", at = @At("HEAD"))
        private static void firstTwoLambdasOrRoot(CallbackInfo ci) {
            print("\tis the root or one of the first 2 lambdas");
        }

        @Inject(method = "runnables ->+ {4}", at = @At("HEAD"))
        private static void firstFourLambdas(CallbackInfo ci) {
            print("\tis one of the first 4 lambdas");
        }

        @Inject(method = "runnables ->{2,} {5}", at = @At("HEAD"))
        private static void firstFiveNestedLambdas(CallbackInfo ci) {
            print("\tis one of the first 5 nested lambdas");
        }

        @Inject(method = "runnables -> ()V ->{0,1} ()V", at = @At("HEAD"))
        private static void singlyOrDoublyNestedLambdasWithDesc(CallbackInfo ci) {
            print("\thas depth 1-2 and only desc ()V along the way");
        }

        @Inject(method = "* ->{0} *", at = @At("HEAD"))
        private static void rootsOnly(CallbackInfo ci) {
            String callerName = Thread.currentThread().getStackTrace()[2].getMethodName();
            print(String.format("// root method %s is running", callerName));
        }

        @Inject(method = "suppliers ->* Ljava/util/function/Supplier;get()Ljava/lang/String;", at = @At("HEAD"))
        private static void stringSuppliers(CallbackInfoReturnable<String> cir) {
            print("\tis a String supplier");
        }

        @Inject(method = "suppliers ->* Ljava/util/function/Supplier;get()Ljava/lang/Integer;", at = @At("HEAD"))
        private static void intSuppliers(CallbackInfoReturnable<Integer> cir) {
            print("\tis an Integer supplier");
        }
    }
}
