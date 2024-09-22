package com.llamalad7.mixintests.ap;

import com.google.auto.service.AutoService;
import com.google.gson.Gson;
import com.llamalad7.mixintests.ap.annotations.RunWithMixins;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@AutoService(Processor.class)
public class MixinConfigGenerator extends AbstractProcessor {
    private static final String ANNOTATION_NAME = RunWithMixins.class.getName();

    private final Gson gson = new Gson();
    private Filer filer;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(
                RunWithMixins.class.getName()
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
        for (Element test : roundEnv.getElementsAnnotatedWith(RunWithMixins.class)) {
            writeMixinConfig((TypeElement) test);
        }
        return false;
    }

    private void writeMixinConfig(TypeElement test) {
        String configName = test.getQualifiedName().toString() + ".mixins.json";
        MixinConfigJson config = new MixinConfigJson(getMixinNames(test));
        String json = gson.toJson(config);
        try {
            FileObject output = filer.createResource(StandardLocation.CLASS_OUTPUT, "", configName);
            try (Writer writer = output.openWriter()) {
                writer.write(json);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private List<String> getMixinNames(TypeElement test) {
        for (AnnotationMirror annotationMirror : test.getAnnotationMirrors()) {
            if (!annotationMirror.getAnnotationType().toString().equals(ANNOTATION_NAME)) {
                continue;
            }
            return annotationMirror.getElementValues().entrySet().stream()
                    .filter(entry -> entry.getKey().getSimpleName().contentEquals("value"))
                    .map(entry -> (List<? extends AnnotationValue>) entry.getValue().getValue())
                    .findAny()
                    .orElseThrow()
                    .stream()
                    .map(it -> (TypeMirror) it.getValue())
                    .map(TypeMirror::toString)
                    .map(it -> it.substring(MixinConfigJson.PACKAGE.length() + 1))
                    .toList();
        }
        throw new NoSuchElementException();
    }
}