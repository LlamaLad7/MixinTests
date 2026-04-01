package com.llamalad7.mixintests.service;

import com.google.auto.service.AutoService;
import org.spongepowered.asm.service.IGlobalPropertyService;
import org.spongepowered.asm.service.IPropertyKey;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unchecked")
@AutoService(IGlobalPropertyService.class)
public class TestGlobalPropertyService implements IGlobalPropertyService {
    private final Map<IPropertyKey, Object> map = new HashMap<>();

    @Override
    public IPropertyKey resolveKey(String name) {
        return new PropertyKey(name);
    }

    @Override
    public <T> T getProperty(IPropertyKey key) {
        return (T) map.get(key);
    }

    @Override
    public void setProperty(IPropertyKey key, Object value) {
        map.put(key, value);
    }

    @Override
    public <T> T getProperty(IPropertyKey key, T defaultValue) {
        return (T) map.getOrDefault(key, defaultValue);
    }

    @Override
    public String getPropertyString(IPropertyKey key, String defaultValue) {
        return getProperty(key, defaultValue);
    }

    private static final class PropertyKey implements IPropertyKey {
        private final String name;

        private PropertyKey(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            PropertyKey that = (PropertyKey) obj;
            return Objects.equals(this.name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        @Override
        public String toString() {
            return "PropertyKey[" +
                    "name=" + name + ']';
        }
    }
}
