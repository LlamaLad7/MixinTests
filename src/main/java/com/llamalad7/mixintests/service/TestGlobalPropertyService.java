package com.llamalad7.mixintests.service;

import com.google.auto.service.AutoService;
import org.spongepowered.asm.service.IGlobalPropertyService;
import org.spongepowered.asm.service.IPropertyKey;

import java.util.HashMap;
import java.util.Map;

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

    private record PropertyKey(String name) implements IPropertyKey {
    }
}
