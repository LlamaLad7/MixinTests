package com.llamalad7.mixintests.ap;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
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
    private InjectorOptions injectors = new InjectorOptions(1);
    private final List<String> mixins;
    private transient final String groupName;

    public MixinTestConfig(TypeElement test) {
        this.pkg = PACKAGE;
        this.mixins = getMixinNames(test);
        this.groupName = test.getQualifiedName().toString();
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

    public String getGroupName() {
        return groupName;
    }

    public String getFileName() {
        return groupName + ".mixins.json";
    }

    private record InjectorOptions(int defaultRequire) {}
}
