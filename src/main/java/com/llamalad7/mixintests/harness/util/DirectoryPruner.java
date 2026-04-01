package com.llamalad7.mixintests.harness.util;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;
import java.util.stream.Collectors;

public class DirectoryPruner {
    private final Set<Path> pathsToKeep;

    public DirectoryPruner(Set<Path> pathsToKeep) {
        this.pathsToKeep = pathsToKeep.stream().map(DirectoryPruner::normalize).collect(Collectors.toSet());
    }

    public void prune(Path rootDir) throws IOException {
        Files.walkFileTree(rootDir, new PruningFileVisitor());
    }

    private static Path normalize(Path path) {
        return path.toAbsolutePath().normalize();
    }

    private class PruningFileVisitor extends SimpleFileVisitor<Path> {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (!pathsToKeep.contains(normalize(file))) {
                if (CiUtil.IS_CI) {
                    throw new IllegalStateException("Found unused test output " + file);
                }
                Files.delete(file);
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            if (exc != null) {
                throw exc;
            }

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
                if (!stream.iterator().hasNext()) {
                    Files.delete(dir);
                }
            }

            return FileVisitResult.CONTINUE;
        }
    }
}
