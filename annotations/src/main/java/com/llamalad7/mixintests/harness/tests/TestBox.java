package com.llamalad7.mixintests.harness.tests;

import java.util.function.Supplier;

public abstract class TestBox {
    private static final ThreadLocal<StringBuilder> builder = ThreadLocal.withInitial(StringBuilder::new);

    protected abstract void box();

    public static void print(Object o) {
        builder.get().append(o).append('\n');
    }

    public static Result run(Supplier<TestBox> box) {
        StringBuilder original = builder.get();
        builder.remove();
        try {
            Throwable error;
            try {
                box.get().box();
                error = null;
            } catch (Throwable e) {
                error = e;
            }
            return new Result(builder.get().toString(), error);
        } finally {
            builder.set(original);
        }
    }

    public static class Result {
        public final String output;
        public final Throwable error;

        public Result(String output, Throwable error) {
            this.output = output;
            this.error = error;
        }
    }
}
