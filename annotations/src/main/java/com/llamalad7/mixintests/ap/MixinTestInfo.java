package com.llamalad7.mixintests.ap;

import com.llamalad7.mixintests.MixinTestConstants;
import com.llamalad7.mixintests.ap.annotations.FabricCompat;
import com.llamalad7.mixintests.ap.annotations.MixinTest;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MixinTestInfo {
    public final List<MixinTestConfig> configs;
    private final TypeElement testClass;

    public MixinTestInfo(TypeElement test, MixinTest annotation) {
        this.testClass = test;
        this.configs = gatherConfigs(test, annotation);
    }

    private static List<MixinTestConfig> gatherConfigs(TypeElement test, MixinTest annotation) {
        Map<Integer, List<String>> mixinsByCompat = new HashMap<>();
        for (Element inner : test.getEnclosedElements()) {
            if (!(inner instanceof TypeElement)) {
                continue;
            }
            TypeElement mixin = (TypeElement) inner;
            FabricCompat compatAnnotation = mixin.getAnnotation(FabricCompat.class);
            Integer compat = compatAnnotation == null ? null : compatAnnotation.value();
            String outer = StringUtils.removeStart(test.getQualifiedName().toString(), MixinTestConstants.PACKAGE + '.');
            mixinsByCompat.computeIfAbsent(compat, k -> new ArrayList<>()).add(outer + "$" + mixin.getSimpleName());
        }
        return mixinsByCompat.entrySet().stream()
                .map(entry -> new MixinTestConfig(test, annotation, entry.getValue(), entry.getKey()))
                .collect(Collectors.toList());
    }

    public String getTestName() {
        return StringUtils.removeStart(testClass.getQualifiedName().toString(), MixinTestConstants.PACKAGE + '.');
    }

    public TypeElement getTestClass() {
        return testClass;
    }
}
