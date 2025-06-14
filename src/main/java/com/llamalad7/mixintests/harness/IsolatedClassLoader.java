package com.llamalad7.mixintests.harness;

import com.github.zafarkhaja.semver.Version;
import com.google.common.collect.Iterators;
import com.llamalad7.mixintests.harness.util.FileUtil;
import com.llamalad7.mixintests.harness.util.MixinVersions;
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
import java.util.function.Function;
import java.util.stream.Stream;

public class IsolatedClassLoader extends URLClassLoader {
    static {
        registerAsParallelCapable();
    }

    private static final URL[] CLASSPATH = getSystemClassPath();
    private static final String MIXIN_PACKAGE = "org.spongepowered.";
    protected static final String MIXIN_SYNTHETIC_PACKAGE = "org.spongepowered.asm.synthetic.";
    private static final String MIXINEXTRAS_PACKAGE = "com.llamalad7.mixinextras.";
    private static final List<String> ISOLATED_PACKAGES = Arrays.asList(
            MIXIN_PACKAGE,
            MIXINEXTRAS_PACKAGE,
            "com.llamalad7.mixintests.service.",
            "com.llamalad7.mixintests.tests."
    );
    private static final Map<String, byte[]> generalClassCache = new ConcurrentHashMap<>();
    private static final Map<Version, Map<String, byte[]>> mixinClassCaches = new ConcurrentHashMap<>();
    private static final Map<Version, Map<String, byte[]>> mixinExtrasClassCaches = new ConcurrentHashMap<>();

    protected final ClassLoader delegate;
    private final Map<String, byte[]> mixinClassCache;
    private final Map<String, byte[]> mixinExtrasClassCache;
    private final ConcurrentMap<String, Class<?>> classes = new ConcurrentHashMap<>();

    public IsolatedClassLoader(String name, ClassLoader parent, MixinVersions mixinVersions) {
        super(name, getClasspath(mixinVersions), new DummyClassLoader());
        delegate = parent;
        if (mixinVersions == null) {
            mixinClassCache = null;
            mixinExtrasClassCache = null;
        } else {
            mixinClassCache = mixinClassCaches.computeIfAbsent(mixinVersions.mixinVersion(), k -> new ConcurrentHashMap<>());
            if (mixinVersions.hasMixinExtras()) {
                mixinExtrasClassCache = mixinExtrasClassCaches.computeIfAbsent(mixinVersions.mixinExtrasVersion(), k -> new ConcurrentHashMap<>());
            } else {
                mixinExtrasClassCache = null;
            }
        }
    }

    private static URL[] getClasspath(MixinVersions mixinVersions) {
        if (mixinVersions == null) {
            return CLASSPATH;
        }
        return Stream.concat(
                Arrays.stream(CLASSPATH),
                mixinVersions.getJars().stream().map(jar -> {
                    try {
                        return jar.toURI().toURL();
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                })
        ).toArray(URL[]::new);
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
        return defineClass(name, bytes, 0, bytes.length);
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
        Map<String, byte[]> cache = chooseClassCache(className);
        Function<String, byte[]> getBytes = name -> {
            URL url = getResource(FileUtil.getClassFileName(name));
            if (url == null) {
                return null;
            }
            try (InputStream stream = url.openStream()) {
                return IOUtils.toByteArray(stream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        return cache == null ? getBytes.apply(className) : cache.computeIfAbsent(className, getBytes);
    }

    private Map<String, byte[]> chooseClassCache(String className) {
        if (className.startsWith(MIXIN_SYNTHETIC_PACKAGE)) {
            // Can't cache these
            return null;
        }
        if (className.startsWith(MIXIN_PACKAGE)) {
            return mixinClassCache;
        }
        if (className.startsWith(MIXINEXTRAS_PACKAGE)) {
            return mixinExtrasClassCache;
        }
        return generalClassCache;
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
