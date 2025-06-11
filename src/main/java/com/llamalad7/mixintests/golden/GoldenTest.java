package com.llamalad7.mixintests.golden;

import com.llamalad7.mixintests.harness.TestResult;
import com.llamalad7.mixintests.harness.util.MixinVersions;
import com.roscopeco.jasm.JasmDisassembler;
import org.apache.commons.lang3.StringUtils;
import org.opentest4j.AssertionFailedError;
import org.opentest4j.FileInfo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class GoldenTest {
    private static final boolean CI = System.getenv("CI") != null;
    private static final boolean FORCE = System.getProperty("forceGoldenTests") != null;
    private static final String TEST_PACKAGE = "com.llamalad7.mixintests.tests.";
    private static final Path OUTPUT_DIR = Path.of("test-outputs");

    private final String testName;
    private final TestResult result;
    private final MixinVersions mixinVersions;
    private final Path baseOutputDir;
    private final Path classOutputDir;

    public GoldenTest(String testName, TestResult result, MixinVersions mixinVersions) {
        this.testName = testName;
        this.result = result;
        this.mixinVersions = mixinVersions;
        this.baseOutputDir = OUTPUT_DIR.resolve(testName.replace('.', '/'));
        this.classOutputDir = chooseClassOutputDir();
    }

    public void test() {
        try {
            doTest();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void doTest() throws IOException {
        checkFile(baseOutputDir.resolve("output.txt"), result.output(), false);
        for (Map.Entry<String, byte[]> entry : result.transformedClasses().entrySet()) {
            String fileName = StringUtils.removeStart(entry.getKey(), TEST_PACKAGE).replace('.', '/') + ".jasm";
            Path output = classOutputDir.resolve(fileName);
            checkFile(output, disassembleClass(entry.getKey(), entry.getValue()), true);
        }
    }

    private String disassembleClass(String name, byte[] bytes) {
        return new JasmDisassembler(
                StringUtils.substringAfterLast(name, '.'),
                false,
                () -> new ByteArrayInputStream(bytes)
        ).disassemble();
    }

    private Path chooseClassOutputDir() {
        return baseOutputDir.resolve("classes").resolve(chooseDirName(mixinVersions));
    }

    private void checkFile(Path file, String actual, boolean canForce) throws IOException {
        actual = GoldenUtils.normalize(actual);
        Files.createDirectories(file.getParent());
        if (!Files.exists(file)) {
            if (!CI) {
                Files.writeString(file, actual);
            }
            throw new AssertionFailedError("Expected file %s did not exist!".formatted(file));
        }
        String expected = GoldenUtils.normalize(Files.readString(file));
        if (!actual.equals(expected)) {
            if (FORCE && canForce) {
                Files.writeString(file, actual);
                return;
            }
            throw new AssertionFailedError(
                    "Content is not equal",
                    new FileInfo(file.toAbsolutePath().toString(), expected.getBytes(StandardCharsets.UTF_8)),
                    actual
            );
        }
    }

    private static String chooseDirName(MixinVersions mixinVersions) {
        String mixinVariant = mixinVersions.isFabric() ? "fabric-mixin" : "mixin";
        String mixinString = mixinVersions.isLatestMixin() ? "latest" : mixinVersions.mixinVersion().toString();
        String mixinExtrasString = mixinVersions.isLatestMixinExtras() ? "latest" : mixinVersions.mixinExtrasVersion().toString();
        return "%s-%s-mixinextras-%s".formatted(mixinVariant, mixinString, mixinExtrasString);
    }
}
