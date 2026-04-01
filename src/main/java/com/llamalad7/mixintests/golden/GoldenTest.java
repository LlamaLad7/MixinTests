package com.llamalad7.mixintests.golden;

import com.llamalad7.mixintests.harness.BuildConstants;
import com.llamalad7.mixintests.harness.TestBootstrap;
import com.llamalad7.mixintests.harness.TestResult;
import com.llamalad7.mixintests.harness.util.CiUtil;
import com.llamalad7.mixintests.harness.util.MixinVersions;
import org.apache.commons.lang3.StringUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceClassVisitor;
import org.opentest4j.AssertionFailedError;
import org.opentest4j.FileInfo;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoldenTest {
    private static final String TEST_PACKAGE = "com.llamalad7.mixintests.tests.";
    private static final Path OUTPUT_DIR = Paths.get(BuildConstants.TEST_OUTPUT_DIR);

    private final TestResult result;
    private final MixinVersions mixinVersions;
    private final Path baseOutputDir;
    private final boolean testBytecode;
    private final Path classOutputDir;
    private final List<Error> errors = new ArrayList<>();

    public GoldenTest(String testName, TestResult result, MixinVersions mixinVersions, boolean testBytecode) {
        this.result = result;
        this.mixinVersions = mixinVersions;
        this.baseOutputDir = OUTPUT_DIR.resolve(testName.replace('.', '/'));
        this.testBytecode = testBytecode;
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
        checkFile(baseOutputDir.resolve("output.txt"), result.output, false);
        if (testBytecode) {
            for (Map.Entry<String, byte[]> entry : result.transformedClasses.entrySet()) {
                String fileName = StringUtils.removeStart(entry.getKey(), TEST_PACKAGE).replace('.', '/') + ".asm";
                Path output = classOutputDir.resolve(fileName);
                checkFile(output, disassembleClass(entry.getKey(), entry.getValue()), true);
            }
        }
    }

    private String disassembleClass(String name, byte[] bytes) {
        StringWriter writer = new StringWriter();
        try (PrintWriter output = new PrintWriter(writer)) {
            TraceClassVisitor traceClassVisitor = new TraceClassVisitor(null, new Textifier(), output);
            new ClassReader(bytes).accept(traceClassVisitor, 0);
            return writer.toString();
        }
    }

    private Path chooseClassOutputDir() {
        return baseOutputDir.resolve("classes").resolve(mixinVersions.getSlug());
    }

    private void checkFile(Path file, String actual, boolean canForce) throws IOException {
        TestBootstrap.touchedOutput(file);
        actual = GoldenUtils.normalize(actual);
        Files.createDirectories(file.getParent());
        if (!Files.exists(file)) {
            if (!CiUtil.IS_CI) {
                Files.write(file, actual.getBytes(StandardCharsets.UTF_8));
            }
            errors.add(new AssertionFailedError(String.format("Expected file %s did not exist!", file)));
            return;
        }
        String expected = GoldenUtils.normalize(new String(Files.readAllBytes(file), StandardCharsets.UTF_8));
        if (!actual.equals(expected)) {
            if (BuildConstants.FORCE_GOLDEN_TESTS && canForce) {
                Files.write(file, actual.getBytes(StandardCharsets.UTF_8));
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
