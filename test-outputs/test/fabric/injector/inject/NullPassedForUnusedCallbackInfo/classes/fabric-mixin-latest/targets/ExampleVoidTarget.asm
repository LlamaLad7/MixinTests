// class version 69.0 (69)
// access flags 0x21
// signature Lcom/llamalad7/mixintests/harness/tests/TestBox;
// declaration: com/llamalad7/mixintests/tests/targets/ExampleVoidTarget extends com.llamalad7.mixintests.harness.tests.TestBox
public class com/llamalad7/mixintests/tests/targets/ExampleVoidTarget extends com/llamalad7/mixintests/harness/tests/TestBox {

  // compiled from: ExampleVoidTarget.java
  // debug info: SMAP
ExampleVoidTarget.java
Mixin
*S Mixin
*F
+ 1 ExampleVoidTarget.java
com/llamalad7/mixintests/tests/targets/ExampleVoidTarget.java
+ 2 NullPassedForUnusedCallbackInfo.java
com/llamalad7/mixintests/tests/mixins/fabric/injector/inject/NullPassedForUnusedCallbackInfo$TheMixin.java
*L
1#1,500:1
1#2,500:501
*E


  // access flags 0x1
  public <init>()V
   L0
    LINENUMBER 5 L0
    ALOAD 0
    INVOKESPECIAL com/llamalad7/mixintests/harness/tests/TestBox.<init> ()V
    RETURN
   L1
    LOCALVARIABLE this Lcom/llamalad7/mixintests/tests/targets/ExampleVoidTarget; L0 L1 0
    MAXSTACK = 1
    MAXLOCALS = 1

  // access flags 0x4
  protected box()V
   L0
    LINENUMBER 8 L0
    LDC "example"
    INVOKESTATIC com/llamalad7/mixintests/tests/targets/ExampleVoidTarget.print (Ljava/lang/Object;)V
   L1
    LINENUMBER 9 L1
    ALOAD 0
    ACONST_NULL
    INVOKESPECIAL com/llamalad7/mixintests/tests/targets/ExampleVoidTarget.handler$zza000$test (Lorg/spongepowered/asm/mixin/injection/callback/CallbackInfo;)V
    RETURN
   L2
    LOCALVARIABLE this Lcom/llamalad7/mixintests/tests/targets/ExampleVoidTarget; L0 L2 0
    MAXSTACK = 2
    MAXLOCALS = 1

  // access flags 0x2
  private handler$zza000$test(Lorg/spongepowered/asm/mixin/injection/callback/CallbackInfo;)V
  @Lorg/spongepowered/asm/mixin/transformer/meta/MixinMerged;(mixin="com.llamalad7.mixintests.tests.mixins.fabric.injector.inject.NullPassedForUnusedCallbackInfo$TheMixin", priority=1000, sessionId="bb20b45f-d4d9-5138-3d93-cb799b3970be")
   L0
    LINENUMBER 518 L0
    LDC "modified"
    INVOKESTATIC com/llamalad7/mixintests/tests/targets/ExampleVoidTarget.print (Ljava/lang/Object;)V
   L1
    LINENUMBER 519 L1
    RETURN
   L2
    LOCALVARIABLE this Lcom/llamalad7/mixintests/tests/targets/ExampleVoidTarget; L0 L2 0
    LOCALVARIABLE ci Lorg/spongepowered/asm/mixin/injection/callback/CallbackInfo; L0 L2 1
    MAXSTACK = 1
    MAXLOCALS = 2
}
