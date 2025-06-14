import com.github.gmazzo.buildconfig.BuildConfigClassSpec
import java.io.Serializable
import kotlin.reflect.KProperty0

inline fun <reified T : Serializable> BuildConfigClassSpec.verbatimPropField(prop: KProperty0<T>) {
    buildConfigField(T::class.java, prop.name, prop.get())
}

fun BuildConfigClassSpec.systemPropField(prop: KProperty0<String>, default: Boolean) {
    buildConfigField(
        Boolean::class.java, prop.name,
        expression("Boolean.parseBoolean(System.getProperty(\"${prop.get()}\", \"$default\"))")
    )
}