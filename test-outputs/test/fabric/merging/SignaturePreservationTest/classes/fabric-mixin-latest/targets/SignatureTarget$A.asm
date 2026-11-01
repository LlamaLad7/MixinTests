// class version 69.0 (69)
// access flags 0x21
// signature <A:Ljava/lang/Object;B:TA;>Ljava/lang/Object;
// declaration: com/llamalad7/mixintests/tests/targets/SignatureTarget$A<A, B extends A>
public class com/llamalad7/mixintests/tests/targets/SignatureTarget$A {

  // compiled from: SignatureTarget.java
  // debug info: SMAP
SignatureTarget.java
Mixin
*S Mixin
*F
+ 1 SignatureTarget.java
com/llamalad7/mixintests/tests/targets/SignatureTarget$A.java
+ 2 SignaturePreservationTest.java
com/llamalad7/mixintests/tests/mixins/fabric/merging/SignaturePreservationTest$TheMixin.java
*L
1#1,500:1
1#2,500:501
*E

  NESTHOST com/llamalad7/mixintests/tests/targets/SignatureTarget
  // access flags 0x9
  public static INNERCLASS com/llamalad7/mixintests/tests/targets/SignatureTarget$A com/llamalad7/mixintests/tests/targets/SignatureTarget A

  // access flags 0x1
  public <init>()V
   L0
    LINENUMBER 13 L0
    ALOAD 0
    INVOKESPECIAL java/lang/Object.<init> ()V
    RETURN
   L1
    LOCALVARIABLE this Lcom/llamalad7/mixintests/tests/targets/SignatureTarget$A; L0 L1 0
    // signature Lcom/llamalad7/mixintests/tests/targets/SignatureTarget$A<TA;TB;>;
    // declaration: this extends com.llamalad7.mixintests.tests.targets.SignatureTarget$A<A, B>
    MAXSTACK = 1
    MAXLOCALS = 1
}
