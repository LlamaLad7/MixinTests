package com.llamalad7.mixintests.golden;

import com.llamalad7.mixintests.harness.BuildConstants;
import com.llamalad7.mixintests.harness.TestBootstrap;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoldenTest {
    private static final boolean CI = System.getenv("CI") != null;
    private static final String TEST_PACKAGE = "com.llamalad7.mixintests.tests.";
    private static final Path OUTPUT_DIR = Path.of(BuildConstants.TEST_OUTPUT_DIR);

    private final TestResult result;
    private final MixinVersions mixinVersions;
    private final Path baseOutputDir;
    private final Path classOutputDir;
    private final List<Error> errors = new ArrayList<>();

    public GoldenTest(String testName, TestResult result, MixinVersions mixinVersions) {
        this.result = result;
        this.mixinVersions = mixinVersions;
        this.baseOutputDir = OUTPUT_DIR.resolve(testName.replace('.', '/'));
        this.classOutputDir = chooseClassOutputDir();
    }

    public void test() {
        try {
            doTest();
            for (Error error : errors) {
                throw error;
            }
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
        return baseOutputDir.resolve("classes").resolve(mixinVersions.getSlug());
    }

    private void checkFile(Path file, String actual, boolean canForce) throws IOException {
        TestBootstrap.touchedOutput(file);
        actual = GoldenUtils.normalize(actual);
        Files.createDirectories(file.getParent());
        if (!Files.exists(file)) {
            if (!CI) {
                Files.writeString(file, actual);
            }
            errors.add(new AssertionFailedError("Expected file %s did not exist!".formatted(file)));
            return;
        }
        String expected = GoldenUtils.normalize(Files.readString(file));
        if (!actual.equals(expected)) {
            if (BuildConstants.FORCE_GOLDEN_TESTS && canForce) {
                Files.writeString(file, actual);
                return;
            }
            errors.add(
                    new AssertionFailedError(
                            "Content is not equal",
                            new FileInfo(file.toAbsolutePath().toString(), expected.getBytes(StandardCharsets.UTF_8)),
                            actual
                    )
            );
        }
    }
}
