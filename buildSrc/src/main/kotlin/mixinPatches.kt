import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.LdcInsnNode
import org.objectweb.asm.tree.MethodInsnNode

private val HOOK_CLASS = Type.getObjectType("com/llamalad7/mixintests/service/MixinHooks")

fun makeUuidsDeterministic(node: ClassNode) {
    for (method in node.methods) {
        for (insn in method.instructions) {
            if (insn is MethodInsnNode && insn.owner == "java/util/UUID" && insn.name == "randomUUID") {
                insn.owner = HOOK_CLASS.internalName
            }
        }
    }
}

fun changeExportDir(node: ClassNode) {
    if (node.name != "org/spongepowered/asm/util/Constants") {
        return
    }
    val clinit = node.methods.first { it.name == "<clinit>" }
    val it = clinit.instructions.iterator()
    while (it.hasNext()) {
        val insn = it.next()
        if (insn is LdcInsnNode && insn.cst == ".mixin.out") {
            it.set(MethodInsnNode(
                Opcodes.INVOKESTATIC,
                HOOK_CLASS.internalName,
                "getMixinOutputDir",
                Type.getMethodDescriptor(Type.getType(String::class.java)),
                false,
            ))
        }
    }
}