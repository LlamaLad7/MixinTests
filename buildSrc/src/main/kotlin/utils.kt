import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.tree.ClassNode
import java.io.File
import java.io.FileOutputStream
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

fun transformJar(inputJar: File, outputJar: File, stubs: Map<String, () -> ClassNode>, transformer: (ClassNode) -> Unit) {
    JarFile(inputJar).use { input ->
        JarOutputStream(FileOutputStream(outputJar)).use { output ->
            val remainingStubs = stubs.toMutableMap()
            for (entry in input.entries()) {
                if (entry.isSignature()) {
                    continue
                }
                val entryInputStream = input.getInputStream(entry)
                output.putNextEntry(ZipEntry(entry.name))

                if (entry.name.endsWith(".class")) {
                    val entryBytes = entryInputStream.readBytes()
                    val node = ClassNode()
                    ClassReader(entryBytes).accept(node, 0)
                    // No need for a stub if the class is already present
                    remainingStubs.remove(node.name)
                    transformer(node)
                    val writer = ClassWriter(ClassWriter.COMPUTE_MAXS)
                    node.accept(writer)
                    output.write(writer.toByteArray())
                } else {
                    entryInputStream.copyTo(output)
                }

                output.closeEntry()
            }

            for ((name, stub) in remainingStubs) {
                output.putNextEntry(ZipEntry("$name.class"))
                val writer = ClassWriter(ClassWriter.COMPUTE_MAXS)
                stub().accept(writer)
                output.write(writer.toByteArray())
                output.closeEntry()
            }
        }
    }
}

private fun ZipEntry.isSignature() =
    name.endsWith(".rsa", ignoreCase = true) || name.endsWith(".sf", ignoreCase = true)