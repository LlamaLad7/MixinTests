package com.llamalad7.mixintests.ap;

import com.llamalad7.mixintests.ap.annotations.TestLocation;
import com.squareup.javapoet.*;
import org.junit.jupiter.api.*;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MixinTestGenerator {
    private static final String TESTS_PACKAGE = "com.llamalad7.mixintests.tests";
    private static final ClassName TEST_BOOTSTRAP = ClassName.get("com.llamalad7.mixintests.harness", "TestBootstrap");
    private static final ClassName TEST_CONFIG = ClassName.get("com.llamalad7.mixintests.harness", "MixinConfig");

    private final List<MixinTestInfo> tests;

    public MixinTestGenerator(List<MixinTestInfo> tests) {
        this.tests = tests;
    }

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
        return tests.stream().map(this::generateTest).collect(Collectors.toList());
    }

    private MethodSpec generateTest(MixinTestInfo test) {
        ClassName testName = ClassName.get(test.getTestClass());
        MethodSpec.Builder builder = MethodSpec.methodBuilder(testMethodName(test.getTestName()))
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
                                .addMember("value", "$S", test.getTestName())
                                .build()
                )
                .addCode(
                        "return $T.doTest($S, $T.class, $T.asList(",
                        TEST_BOOTSTRAP,
                        test.getTestName(),
                        testName,
                        Arrays.class
                );
        List<MixinTestConfig> configs = test.configs;
        for (int i = 0; i < configs.size(); i++) {
            if (i != 0) {
                builder.addCode(", ");
            }
            MixinTestConfig config = configs.get(i);
            builder.addCode("new $T($S, $L)", TEST_CONFIG, config.getFileName(), config.getFabricCompat());
        }
        return builder.addCode("));").build();
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
