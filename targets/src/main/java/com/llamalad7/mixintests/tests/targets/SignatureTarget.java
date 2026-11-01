package com.llamalad7.mixintests.tests.targets;

import com.llamalad7.mixintests.harness.tests.TestBox;

import java.util.function.Consumer;

public class SignatureTarget extends TestBox {
    @Override
    protected void box() {
        this.getClass().getClasses();
    }

    public static class A<A, B extends A> {
    }

    public static class B<A extends B, B> {
    }

    public static class C<A extends C, B, C> {
    }

    public static abstract class D<A> implements Consumer<A[][]> {
    }

    public static abstract class E<A> extends D<A[][]> implements Consumer<A[][][][]> {
    }
}
