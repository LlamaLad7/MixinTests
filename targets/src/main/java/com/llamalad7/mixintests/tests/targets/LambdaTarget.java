package com.llamalad7.mixintests.tests.targets;

import com.llamalad7.mixintests.harness.tests.TestBox;

import java.util.function.Supplier;

public class LambdaTarget extends TestBox {
    @Override
    protected void box() {
        print("Runnables:");
        runnables();
        print("Suppliers:");
        suppliers();
    }

    private void runnables() {
        int dummyCapture = 5;

        Runnable a = () -> {
            Runnable b = () -> {
                int _ = dummyCapture;
                Runnable c = () -> {
                };

                print("A.B.C:");
                c.run();
            };

            Runnable d = () -> {
            };

            print("A.B:");
            b.run();
            print("A.D:");
            d.run();
        };

        Runnable e = () -> {
            Runnable f = () -> {
            };
            Runnable g = () -> {
                Runnable h = () -> {
                    int _ = dummyCapture;
                };

                print("E.G.H:");
                h.run();
            };

            print("E.F:");
            f.run();
            print("E.G:");
            g.run();
        };

        print("A:");
        a.run();
        print("E:");
        e.run();
    }

    private void suppliers() {
        int dummyCapture = 5;

        Supplier<Integer> a = () -> {
            Supplier<String> b = () -> {
                int _ = dummyCapture;
                return "b";
            };

            print("A.B:");
            b.get();
            return (int) 'a';
        };

        Supplier<String> c = () -> "c";

        print("A:");
        a.get();
        print("C:");
        c.get();
    }
}