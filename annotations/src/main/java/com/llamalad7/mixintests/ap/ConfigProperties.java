package com.llamalad7.mixintests.ap;

import com.llamalad7.mixintests.ap.annotations.Config;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.Objects;

public class ConfigProperties {
    public final String id;
    public final int fabricCompat;
    public final String plugin;

    public ConfigProperties(ProcessingEnvironment processingEnv, Config annotation) {
        if (annotation == null) {
            this.id = "";
            this.fabricCompat = Integer.MAX_VALUE;
            this.plugin = null;
            return;
        }
        this.id = annotation.id();
        this.fabricCompat = annotation.fabricCompat();
        try {
            annotation.plugin();
            throw new AssertionError("unreachable");
        } catch (MirroredTypeException e) {
            TypeMirror type = e.getTypeMirror();
            if (type.getKind() == TypeKind.VOID) {
                this.plugin = null;
            } else {
                this.plugin = processingEnv.getElementUtils().getBinaryName((TypeElement) ((DeclaredType) e.getTypeMirror()).asElement()).toString();
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ConfigProperties that = (ConfigProperties) o;
        return fabricCompat == that.fabricCompat && Objects.equals(id, that.id) && Objects.equals(plugin, that.plugin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fabricCompat, plugin);
    }
}
