package com.llamalad7.mixintests.harness;

import com.google.common.collect.Iterators;
import com.llamalad7.mixintests.harness.util.FileUtil;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.cert.Certificate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class IsolatedClassLoader extends URLClassLoader {
    static {
        registerAsParallelCapable();
    }

    private static final URL[] CLASSPATH = getSystemClassPath();
    private static final List<String> ISOLATED_PACKAGES = Arrays.asList(
            "org.spongepowered.",
            "com.llamalad7.mixintests.service.",
            "com.llamalad7.mixintests.tests."
    );
    private static final Map<String, byte[]> classCache = new ConcurrentHashMap<>();

    protected final ClassLoader delegate;
    private final ConcurrentMap<String, Class<?>> classes = new ConcurrentHashMap<>();

    public IsolatedClassLoader(String name, ClassLoader parent) {
        super(name, CLASSPATH, new DummyClassLoader());
        delegate = parent;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class<?> clazz = classes.get(name);
        if (clazz != null) {
            return clazz;
        }
        if (!shouldLoad(name)) {
            clazz = delegate.loadClass(name);
            classes.put(name, clazz);
            return clazz;
        }
        synchronized (getClassLoadingLock(name)) {
            clazz = findLoadedClass(name);
            if (clazz == null) {
                clazz = tryLoadClass(name);
            }
            classes.put(name, clazz);
            return clazz;
        }
    }

    private Class<?> tryLoadClass(String name) throws ClassNotFoundException {
        byte[] bytes = getClassBytes(name);
        if (bytes == null) {
            throw new ClassNotFoundException(name);
        }
        int lastDot = name.lastIndexOf('.');

        if (lastDot > 0) {
            String packageName = name.substring(0, lastDot);

            if (getDefinedPackage(packageName) == null) {
                try {
                    definePackage(packageName, null, null, null, null, null, null, null);
                } catch (IllegalArgumentException e) { // presumably concurrent package definition
                    if (getDefinedPackage(packageName) == null) throw e; // still not defined?
                }
            }
        }
        return defineClass(name, bytes, 0, bytes.length, getCodeSource(name));
    }

    private CodeSource getCodeSource(String className) {
        String fileName = FileUtil.getClassFileName(className);
        URL url = getResource(fileName);
        try {
            return new CodeSource(FileUtil.getCodeSource(url, fileName).toUri().toURL(), (Certificate[]) null);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    protected byte[] getClassBytes(String className) {
        return classCache.computeIfAbsent(className, name -> {
            URL url = getResource(FileUtil.getClassFileName(name));
            if (url == null) {
                return null;
            }
            try (InputStream stream = url.openStream()) {
                return IOUtils.toByteArray(stream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    protected boolean shouldLoad(String className) {
        return ISOLATED_PACKAGES.stream().anyMatch(className::startsWith);
    }

    @Override
    public URL getResource(String name) {
        URL url = findResource(name);
        if (url != null) {
            return url;
        }

        return delegate.getResource(name);
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        return Iterators.asEnumeration(
                Iterators.concat(
                        super.getResources(name).asIterator(),
                        delegate.getResources(name).asIterator()
                )
        );
    }

    private static URL[] getSystemClassPath() {
        try {
            String[] paths = ManagementFactory.getRuntimeMXBean().getClassPath().split(":");
            List<URL> result = new ArrayList<>();
            for (String path : paths) {
                result.add(new File(path).toURI().toURL());
            }
            return result.toArray(URL[]::new);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static class DummyClassLoader extends ClassLoader {
        @Override
        protected Package[] getPackages() {
            return null;
        }

        @Override
        protected Package getPackage(String name) {
            return null;
        }

        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            throw new ClassNotFoundException();
        }

        @Override
        public URL getResource(String name) {
            return null;
        }

        @Override
        public Enumeration<URL> getResources(String name) {
            return Collections.emptyEnumeration();
        }
    }
}
