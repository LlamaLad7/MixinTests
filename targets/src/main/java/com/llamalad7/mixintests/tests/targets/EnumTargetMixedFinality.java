package com.llamalad7.mixintests.tests.targets;

import com.llamalad7.mixintests.harness.tests.TestBox;

public class EnumTargetMixedFinality extends TestBox {
    @Override
    protected void box() {
        print("Non Final");
        for (NonFinalEnum e : NonFinalEnum.values()) {
            print(e.name() + ": " + e.favouriteNumber());
        }
        print("Final");
        for (FinalEnum e : FinalEnum.values()) {
            print(e.name() + ": " + e);
        }
    }

    public enum NonFinalEnum {
        A {
            @Override
            protected int favouriteNumber() {
                return 100;
            }
        }, B, C;

        protected int favouriteNumber() {
            return ordinal();
        }
    }

    public enum FinalEnum {
        A, B, C
    }
}
