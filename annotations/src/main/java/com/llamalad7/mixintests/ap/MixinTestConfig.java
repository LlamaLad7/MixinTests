package com.llamalad7.mixintests.ap;

import com.google.gson.annotations.SerializedName;
import com.llamalad7.mixintests.ap.annotations.MixinTest;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.List;

public class MixinTestConfig {
    private static final String PACKAGE = "com.llamalad7.mixintests.tests.mixins";
    @SerializedName("package")
    private final String pkg;
    private final String minVersion = "0.8";
    private final String compatibilityLevel = "JAVA_11";
    private final boolean required = true;
    private final InjectorOptions injectors = new InjectorOptions(1);
    private final MixinExtrasOptions mixinextras;
    private final List<String> mixins;
    private transient final TypeElement testClass;

    public MixinTestConfig(TypeElement test, MixinTest annotation) {
        this.pkg = PACKAGE;
        this.mixins = getMixinNames(test);
        this.testClass = test;
        mixinextras = new MixinExtrasOptions(annotation.minMixinExtras());
    }

    private static List<String> getMixinNames(TypeElement test) {
        List<String> result = new ArrayList<>();
        for (Element inner : test.getEnclosedElements()) {
            if (!(inner instanceof TypeElement mixin)) {
                continue;
            }
            String outer = StringUtils.removeStart(test.getQualifiedName().toString(), PACKAGE + '.');
            result.add(outer + "$" + mixin.getSimpleName());
        }
        return result;
    }

    public String getTestName() {
        return StringUtils.removeStart(testClass.getQualifiedName().toString(), PACKAGE + '.');
    }

    public String getFileName() {
        return testClass.getQualifiedName().toString() + ".mixins.json";
    }

    public TypeElement getTestClass() {
        return testClass;
    }

    private record InjectorOptions(int defaultRequire) {
    }

    private record MixinExtrasOptions(String minVersion) {
    }
}
