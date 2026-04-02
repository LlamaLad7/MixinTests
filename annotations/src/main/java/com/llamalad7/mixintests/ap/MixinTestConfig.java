package com.llamalad7.mixintests.ap;

import com.google.gson.annotations.SerializedName;
import com.llamalad7.mixintests.MixinTestConstants;
import com.llamalad7.mixintests.ap.annotations.MixinTest;

import javax.lang.model.element.TypeElement;
import java.util.List;

public class MixinTestConfig {
    @SerializedName("package")
    private final String pkg;
    private final String minVersion = "0.8";
    private final String compatibilityLevel = "JAVA_11";
    private final boolean required = true;
    private final InjectorOptions injectors = new InjectorOptions(1);
    private final MixinExtrasOptions mixinextras;
    private final List<String> mixins;
    private transient final TypeElement testClass;
    private transient final Integer fabricCompat;

    public MixinTestConfig(TypeElement test, MixinTest annotation, List<String> mixinNames, Integer fabricCompat) {
        this.pkg = MixinTestConstants.PACKAGE;
        this.mixins = mixinNames;
        this.testClass = test;
        this.fabricCompat = fabricCompat;
        mixinextras = new MixinExtrasOptions(annotation.minMixinExtras());
    }

    public String getFileName() {
        return testClass.getQualifiedName() + "-" + fabricCompat + ".mixins.json";
    }

    public Integer getFabricCompat() {
        return fabricCompat;
    }

    private static final class InjectorOptions {
        public final int defaultRequire;

        public InjectorOptions(int defaultRequire) {
            this.defaultRequire = defaultRequire;
        }
    }

    private static final class MixinExtrasOptions {
        public final String minVersion;

        public MixinExtrasOptions(String minVersion) {
            this.minVersion = minVersion;
        }
    }
}
