package com.llamalad7.mixintests.ap;

import com.llamalad7.mixintests.ap.annotations.TestLocation;
import com.palantir.javapoet.*;
import org.junit.jupiter.api.*;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public record MixinTestGenerator(List<MixinTestConfig> configs) {
    private static final String TESTS_PACKAGE = "com.llamalad7.mixintests.tests";
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
                .addMethod(generateBeforeTestsMethod())
                .addMethod(generateAfterTestsMethod())
                .addMethods(generateMethods())
                .build();

        return JavaFile.builder(TESTS_PACKAGE, testClass)
                .build();
    }

    private List<MethodSpec> generateMethods() {
        return configs.stream().map(this::generateTest).toList();
    }

    private MethodSpec generateTest(MixinTestConfig config) {
        ClassName testName = ClassName.get(config.getTestClass());
        return MethodSpec.methodBuilder(testMethodName(config.getTestName()))
                .returns(ParameterizedTypeName.get(Stream.class, DynamicTest.class))
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(TestFactory.class)
                .addAnnotation(
                        AnnotationSpec.builder(TestLocation.class)
                                .addMember("value", "$T.class", testName)
                                .build()
                )
                .addAnnotation(
                        AnnotationSpec.builder(DisplayName.class)
                                .addMember("value", "$S", config.getTestName())
                                .build()
                )
                .addCode(
                        "return $T.doTest($S, $S, $T.class);",
                        TEST_BOOTSTRAP,
                        config.getTestName(),
                        config.getFileName(),
                        testName
                )
                .build();
    }

    private String testMethodName(String testName) {
        String[] parts = testName.split("\\.");
        return "test$" + String.join("$", parts);
    }

    private MethodSpec generateBeforeTestsMethod() {
        return MethodSpec.methodBuilder("beforeTests")
                .returns(TypeName.VOID)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addAnnotation(BeforeAll.class)
                .addCode("$T.beforeTests();", TEST_BOOTSTRAP)
                .build();
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
