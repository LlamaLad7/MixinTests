package com.llamalad7.mixintests.harness;

import com.google.common.jimfs.Jimfs;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class InMemoryClassPath {
    private static final FileSystem fs = Jimfs.newFileSystem();
    private static final List<URL> urls = new ArrayList<>();

    static {
        try {
            String[] paths = ManagementFactory.getRuntimeMXBean().getClassPath().split(":");
            for (String path : paths) {
                load(path);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create in-memory classpath: ", e);
        }
    }

    public static URL[] getUrls() {
        return urls.toArray(URL[]::new);
    }

    private static void load(String pathStr) throws Exception {
        Path path = Paths.get(pathStr);
        if (Files.isDirectory(path)) {
            loadDir(path);
        } else {
            loadFile(path);
        }
    }

    private static void loadDir(Path dir) throws Exception {
        Path memoryDir = fs.getPath(dir.toAbsolutePath().toString());
        Files.createDirectories(memoryDir);
        try (Stream<Path> paths = Files.walk(dir)) {
            paths.forEach(sourcePath -> {
                try {
                    Path targetPath = memoryDir.resolve(dir.relativize(sourcePath).toString());

                    if (Files.isDirectory(sourcePath)) {
                        Files.createDirectories(targetPath);
                    } else {
                        Files.copy(sourcePath, targetPath);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        urls.add(memoryDir.toUri().toURL());
    }

    private static void loadFile(Path file) throws Exception {
        Path dir = file.getParent();
        Path memoryDir = fs.getPath(dir.toAbsolutePath().toString());
        Files.createDirectories(memoryDir);
        Path memoryFile = memoryDir.resolve(file.getFileName().toString());
        Files.copy(file, memoryFile);
        urls.add(memoryFile.toUri().toURL());
    }
}
