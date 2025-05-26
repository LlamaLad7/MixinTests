package com.llamalad7.mixintests.ap;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeSpec;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public record MixinTestGenerator(List<MixinTestConfig> configs) {
    private static final String TESTS_PACKAGE = "com.llamalad7.mixintests.tests";
    private static final String MIXINS_PACKAGE = TESTS_PACKAGE + ".mixins";
    private static final ClassName TEST_ANNOTATION = ClassName.get("org.junit.jupiter.api", "Test");
    private static final ClassName TEST_BOOTSTRAP = ClassName.get("com.llamalad7.mixintests.harness", "TestBootstrap");

    public void generate(Filer filer) {
        JavaFile javaFile = generateFile();

        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JavaFile generateFile() {
        TypeSpec testClass = TypeSpec.classBuilder("GeneratedMixinTests")
                .addModifiers(Modifier.PUBLIC)
                .addMethods(generateMethods())
                .build();

        return JavaFile.builder(TESTS_PACKAGE, testClass)
                .build();
    }

    private List<MethodSpec> generateMethods() {
        return configs.stream().map(this::generateTest).toList();
    }

    private MethodSpec generateTest(MixinTestConfig config) {
        return MethodSpec.methodBuilder(testMethodName(config.getGroupName()))
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(TEST_ANNOTATION)
                .addCode("$T.doTest($S);", TEST_BOOTSTRAP, config.getFileName())
                .build();
    }

    private String testMethodName(String configName) {
        String shortName = StringUtils.removeStart(configName, MIXINS_PACKAGE + '.');
        String[] parts = shortName.split("\\.");
        return "test" + String.join("", Arrays.stream(parts).map(StringUtils::capitalize).toList());
    }
}
