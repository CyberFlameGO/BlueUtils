package net.axay.blueutils.hocon

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigRenderOptions
import io.github.config4k.extract
import io.github.config4k.toConfig
import net.axay.blueutils.files.createIfNotExists
import java.io.File
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Creates a HOCON config delegate. This delegate is behaving lazy.
 * @param file the config file
 * @param name the name of the config (default is equal to the name of the file)
 * @param hocon if true, HOCON (a superset of jsom) will be used
 * @param pretty if true, the output will be pretty formatted
 */
inline fun <reified T : Any> hoconConfig(
    file: File,
    name: String = file.nameWithoutExtension,
    hocon: Boolean = true,
    pretty: Boolean = true,
    noinline default: (() -> T)? = null
) = object : HOCONDelegate<T>(
    file, name, ConfigRenderOptions.concise().setJson(!hocon).setFormatted(pretty), default
) {
    override fun excractFromFile(config: Config) = config.extract<T>(name)
}

/**
 * The delegate for a HOCON config.
 * You probably want to use this with the [hoconConfig] function.
 */
abstract class HOCONDelegate<T : Any>(
    private val file: File,
    private val name: String,
    private val renderOptions: ConfigRenderOptions,
    private val default: (() -> T)?
) : ReadWriteProperty<Any?, T> {
    private var _internalConfig: T? = null
    private var internalConfig: T
        get() {
            if (_internalConfig == null) {
                if (file.createIfNotExists()) {
                    saveConfig(
                        default?.invoke()
                            ?: error("Config file does not exists and no default config is available")
                    )
                } else _internalConfig = excractFromFile(ConfigFactory.parseFile(file))
            }
            return _internalConfig!!
        }
        set(value) = saveConfig(value)

    private fun saveConfig(value: T) {
        _internalConfig = value
        file.writeText(value.toConfig(name).root().render(renderOptions))
    }

    protected abstract fun excractFromFile(config: Config): T

    override operator fun getValue(thisRef: Any?, property: KProperty<*>) = internalConfig
    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        internalConfig = value
    }
}
