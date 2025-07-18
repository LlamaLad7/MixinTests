package com.llamalad7.mixintests.ap;

import com.google.auto.service.AutoService;
import com.google.gson.Gson;
import com.llamalad7.mixintests.ap.annotations.MixinTest;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
public class MixinTestsAP extends AbstractProcessor {
    private static final Class<MixinTest> ANNOTATION = MixinTest.class;
    private static final String ANNOTATION_NAME = ANNOTATION.getName();

    private final Gson gson = new Gson();
    private final List<MixinTestConfig> configs = new ArrayList<>();
    private Filer filer;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(
                ANNOTATION_NAME
        );
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return false;
        }
        gatherConfigs(roundEnv);
        writeOutputs();
        return false;
    }

    private void gatherConfigs(RoundEnvironment roundEnv) {
        for (Element test : roundEnv.getElementsAnnotatedWith(ANNOTATION)) {
            configs.add(new MixinTestConfig((TypeElement) test, test.getAnnotation(ANNOTATION)));
        }
    }

    private void writeOutputs() {
        if (configs.isEmpty()) {
            return;
        }
        writeConfigJsons();
        writeTestClass();
        configs.clear();
    }

    private void writeConfigJsons() {
        for (MixinTestConfig config : configs) {
            try {
                FileObject resource = filer.createResource(StandardLocation.CLASS_OUTPUT, "", config.getFileName());
                try (Writer writer = resource.openWriter()) {
                    gson.toJson(config, writer);
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to write mixin config: ", e);
            }
        }
    }

    private void writeTestClass() {
        new MixinTestGenerator(configs).generate(filer);
    }
}