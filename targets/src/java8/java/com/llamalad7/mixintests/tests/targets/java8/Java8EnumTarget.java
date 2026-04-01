package com.llamalad7.mixintests.tests.targets.java8;

import com.llamalad7.mixintests.harness.tests.TestBox;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Java8EnumTarget extends TestBox {
    @Override
    protected void box() {
        print("Normal (modified " + NormalEnum.modifiedCount + ") times");
        for (NormalEnum e : NormalEnum.values()) {
            print(e.name());
            assertEquals(e.ordinal(), e.inferredOrdinal);
            assertEquals(e, NormalEnum.valueOf(e.name()));
            assertEquals(e, NormalEnum.values()[e.ordinal()]);
        }
        print("Abstract (modified " + AbstractEnum.modifiedCount + ") times");
        for (AbstractEnum e : AbstractEnum.values()) {
            print(e.name());
            assertEquals(e.ordinal(), e.inferredOrdinal);
            assertEquals(e.name(), e.inferredName());
            assertEquals(e, AbstractEnum.valueOf(e.name()));
            assertEquals(e, AbstractEnum.values()[e.ordinal()]);
        }
    }

    public enum NormalEnum {
        A(0), B(1), C(2);

        private static int modifiedCount = 0;

        public final int inferredOrdinal;

        NormalEnum(int inferredOrdinal) {
            this.inferredOrdinal = inferredOrdinal;
        }

        private static void clinitMarker() {
            modifiedCount++;
        }
    }

    public enum AbstractEnum {
        A(0) {
            @Override
            public String inferredName() {
                return "A";
            }
        },
        B(1) {
            @Override
            public String inferredName() {
                return "B";
            }
        },
        C(2) {
            @Override
            public String inferredName() {
                return "C";
            }
        };

        private static int modifiedCount = 0;

        public final int inferredOrdinal;

        AbstractEnum(int inferredOrdinal) {
            this.inferredOrdinal = inferredOrdinal;
        }

        public abstract String inferredName();

        private static void clinitMarker() {
            modifiedCount++;
        }
    }
}
