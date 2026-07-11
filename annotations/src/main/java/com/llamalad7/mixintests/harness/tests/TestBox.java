package com.llamalad7.mixintests.harness.tests;

import java.util.function.Supplier;

public abstract class TestBox {
    private static final ThreadLocal<StringBuilder> builder = ThreadLocal.withInitial(StringBuilder::new);

    protected abstract void box();

    public static void print(Object o) {
        builder.get().append(o).append('\n');
    }

    public static String run(Supplier<TestBox> box) {
        StringBuilder original = builder.get();
        builder.remove();
        try {
            box.get().box();
            return builder.get().toString();
        } finally {
            builder.set(original);
        }
    }
}
