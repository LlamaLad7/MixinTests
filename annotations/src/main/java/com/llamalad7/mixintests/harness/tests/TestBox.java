package com.llamalad7.mixintests.harness.tests;

public abstract class TestBox {
    private final StringBuilder builder = new StringBuilder();

    protected abstract void box();

    protected final void print(Object o) {
        builder.append(o).append('\n');
    }

    public final String run() {
        builder.setLength(0);
        box();
        return builder.toString();
    }
}
