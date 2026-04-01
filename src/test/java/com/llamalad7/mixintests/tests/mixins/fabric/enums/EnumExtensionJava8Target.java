package com.llamalad7.mixintests.tests.mixins.fabric.enums;

import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.ap.annotations.TestOption;
import com.llamalad7.mixintests.tests.targets.java8.Java8EnumTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.MixinIntrinsics;
import org.spongepowered.asm.mixin.Shadow;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MixinTest(box = Java8EnumTarget.class, fabricMixin = TestOption.ON, minFabricMixin = "0.17.1")
public class EnumExtensionJava8Target {
    @Mixin(Java8EnumTarget.NormalEnum.class)
    enum NormalMixin {
        CUSTOM1(MixinIntrinsics.currentEnumOrdinal()), CUSTOM2(MixinIntrinsics.currentEnumOrdinal());

        @Shadow
        NormalMixin(int inferredOrdinal) {
        }

        @Shadow
        protected static void clinitMarker() {}

        static {
            clinitMarker();
            assertEquals(7, values().length);
        }
    }

    @Mixin(Java8EnumTarget.NormalEnum.class)
    enum NormalMixin2 {
        CUSTOM3(MixinIntrinsics.currentEnumOrdinal()), CUSTOM4(MixinIntrinsics.currentEnumOrdinal());

        @Shadow
        NormalMixin2(int inferredOrdinal) {
        }

        @Shadow
        protected static void clinitMarker() {}

        static {
            clinitMarker();
            assertEquals(7, values().length);
        }
    }

    @Mixin(Java8EnumTarget.AbstractEnum.class)
    enum AbstractMixin {
        CUSTOM1(MixinIntrinsics.currentEnumOrdinal()) {
            @Override
            public String inferredName() {
                return "CUSTOM1";
            }
        },
        CUSTOM2(MixinIntrinsics.currentEnumOrdinal()) {
            @Override
            public String inferredName() {
                return "CUSTOM2";
            }
        };

        @Shadow
        AbstractMixin(int inferredOrdinal) {
        }

        @Shadow
        public abstract String inferredName();

        @Shadow
        protected static void clinitMarker() {}

        static {
            clinitMarker();
            assertEquals(7, values().length);
        }
    }

    @Mixin(Java8EnumTarget.AbstractEnum.class)
    enum AbstractMixin2 {
        CUSTOM3(MixinIntrinsics.currentEnumOrdinal()) {
            @Override
            public String inferredName() {
                return "CUSTOM3";
            }
        },
        CUSTOM4(MixinIntrinsics.currentEnumOrdinal()) {
            @Override
            public String inferredName() {
                return "CUSTOM4";
            }
        };

        @Shadow
        AbstractMixin2(int inferredOrdinal) {
        }

        @Shadow
        public abstract String inferredName();

        @Shadow
        protected static void clinitMarker() {}

        static {
            clinitMarker();
            assertEquals(7, values().length);
        }
    }
}
