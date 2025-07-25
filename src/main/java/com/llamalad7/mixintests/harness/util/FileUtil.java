/*
 * Copyright 2016 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.llamalad7.mixintests.harness.util;

import java.io.File;
import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;

public final class FileUtil {
    public static final Path LOADER_CODE_SOURCE = getCodeSource(FileUtil.class);

    public static Path getCodeSource(URL url, String localPath) {
        try {
            URLConnection connection = url.openConnection();

            if (connection instanceof JarURLConnection) {
                return asPath(((JarURLConnection) connection).getJarFileURL());
            } else {
                String path = url.getPath();

                if (path.endsWith(localPath)) {
                    return asPath(new URL(url.getProtocol(), url.getHost(), url.getPort(), path.substring(0, path.length() - localPath.length())));
                } else {
                    throw new RuntimeException("Could not figure out code source for file '" + localPath + "' in URL '" + url + "'!");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Path asPath(URL url) {
        try {
            return Paths.get(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static URL asUrl(File file) throws MalformedURLException {
        return file.toURI().toURL();
    }

    public static URL asUrl(Path path) throws MalformedURLException {
        return path.toUri().toURL();
    }

    public static Path getCodeSource(Class<?> cls) {
        CodeSource cs = cls.getProtectionDomain().getCodeSource();
        if (cs == null) return null;

        return asPath(cs.getLocation());
    }

    public static String getClassFileName(String className) {
        return className.replace('.', '/') + ".class";
    }
}
