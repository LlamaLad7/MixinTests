package com.llamalad7.mixintests.harness.util;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

public class MixinClassDetector {
    public static boolean hasMixins(byte[] classBytes) {
        if (classBytes == null) {
            return false;
        }
        String[] debugInfo = {null};
        new ClassReader(classBytes).accept(new ClassVisitor(Opcodes.ASM9) {
            @Override
            public void visitSource(String source, String debug) {
                debugInfo[0] = debug;
            }
        }, ClassReader.SKIP_CODE);
        if (debugInfo[0] == null || !debugInfo[0].startsWith("SMAP")) {
            return false;
        }
        String[] smapLines = debugInfo[0].split("\n");
        if (smapLines.length < 3) {
            return false;
        }
        return smapLines[2].equals("Mixin");
    }
}
