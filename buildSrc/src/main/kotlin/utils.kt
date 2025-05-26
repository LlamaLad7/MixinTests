import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.tree.ClassNode
import java.io.File
import java.io.FileOutputStream
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

fun transformJar(inputJar: File, outputJar: File, transformer: (ClassNode) -> Unit) {
    JarFile(inputJar).use { input ->
        JarOutputStream(FileOutputStream(outputJar)).use { output ->
            for (entry in input.entries()) {
                if (entry.isSignature()) {
                    continue
                }
                val entryInputStream = input.getInputStream(entry)
                val entryBytes = entryInputStream.readBytes()

                if (entry.name.endsWith(".class")) {
                    val node = ClassNode()
                    ClassReader(entryBytes).accept(node, 0)
                    transformer(node)
                    val writer = ClassWriter(ClassWriter.COMPUTE_MAXS)
                    node.accept(writer)

                    val newEntry = ZipEntry(entry.name)
                    output.putNextEntry(newEntry)
                    output.write(writer.toByteArray())
                    output.closeEntry()
                } else {
                    output.putNextEntry(ZipEntry(entry.name))
                    output.write(entryBytes)
                    output.closeEntry()
                }
            }
        }
    }
}

private fun ZipEntry.isSignature() =
    name.endsWith(".rsa", ignoreCase = true) || name.endsWith(".sf", ignoreCase = true)