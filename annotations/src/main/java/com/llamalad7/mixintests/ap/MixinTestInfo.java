package com.llamalad7.mixintests.ap;

import com.llamalad7.mixintests.MixinTestConstants;
import com.llamalad7.mixintests.ap.annotations.Config;
import com.llamalad7.mixintests.ap.annotations.MixinTest;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MixinTestInfo {
    private static final String MIXIN_ANNOTATION = "org.spongepowered.asm.mixin.Mixin";

    public final List<MixinTestConfig> configs;
    private final TypeElement testClass;

    public MixinTestInfo(ProcessingEnvironment processingEnv, TypeElement test, MixinTest annotation) {
        this.testClass = test;
        this.configs = gatherConfigs(processingEnv, test, annotation);
    }

    private static List<MixinTestConfig> gatherConfigs(ProcessingEnvironment processingEnv, TypeElement test, MixinTest annotation) {
        Map<ConfigProperties, List<String>> mixinsByConfig = new HashMap<>();
        for (Element inner : test.getEnclosedElements()) {
            if (!(inner instanceof TypeElement)) {
                continue;
            }
            TypeElement mixin = (TypeElement) inner;
            if (mixin.getAnnotationMirrors().stream().noneMatch(it -> it.getAnnotationType().toString().equals(MIXIN_ANNOTATION))) {
                continue;
            }
            ConfigProperties configProperties = new ConfigProperties(processingEnv, mixin.getAnnotation(Config.class));
            String mixinName = StringUtils.removeStart(processingEnv.getElementUtils().getBinaryName(mixin).toString(), MixinTestConstants.PACKAGE + '.');
            mixinsByConfig.computeIfAbsent(configProperties, k -> new ArrayList<>()).add(mixinName);
        }
        return mixinsByConfig.entrySet().stream()
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
