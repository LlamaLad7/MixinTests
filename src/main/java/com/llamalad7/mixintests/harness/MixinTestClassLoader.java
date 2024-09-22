package com.llamalad7.mixintests.harness;

import java.net.URL;
import java.net.URLClassLoader;

public class MixinTestClassLoader extends URLClassLoader {
    public MixinTestClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }
}
