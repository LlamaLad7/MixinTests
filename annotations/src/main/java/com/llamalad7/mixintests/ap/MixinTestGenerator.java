package com.llamalad7.mixintests.ap;

import com.palantir.javapoet.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public record MixinTestGenerator(List<MixinTestConfig> configs) {
    private static final String TESTS_PACKAGE = "com.llamalad7.mixintests.tests";
    private static final String MIXINS_PACKAGE = TESTS_PACKAGE + ".mixins";
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
                .addMethod(generateAfterTestsMethod())
                .build();

        return JavaFile.builder(TESTS_PACKAGE, testClass)
                .build();
    }

    private List<MethodSpec> generateMethods() {
        return configs.stream().map(this::generateTest).toList();
    }

    private MethodSpec generateTest(MixinTestConfig config) {
        return MethodSpec.methodBuilder(testMethodName(config.getTestName()))
                .returns(ParameterizedTypeName.get(Stream.class, DynamicTest.class))
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(TestFactory.class)
                .addCode(
                        "return $T.doTest($S, $S, $T.class);",
                        TEST_BOOTSTRAP,
                        config.getTestName(),
                        config.getFileName(),
                        ClassName.get(config.getTestClass())
                )
                .build();
    }

    private String testMethodName(String testName) {
        String[] parts = testName.split("\\.");
        return "test" + String.join("", Arrays.stream(parts).map(StringUtils::capitalize).toList());
    }

    private MethodSpec generateAfterTestsMethod() {
        return MethodSpec.methodBuilder("afterTests")
                .returns(TypeName.VOID)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addAnnotation(AfterAll.class)
                .addCode("$T.afterTests();", TEST_BOOTSTRAP)
                .build();
    }
}
