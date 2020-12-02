package net.axay.blueutils.gson

import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Creates a new ConfigDelegate object.
 *
 * You can use this as a delegate (with **by**) or
 * a normal object.
 * (Delegate allows direct access to the config
 * object, but does not provide save() or reload()
 * methods.)
 *
 * @param T The class type of the config.
 * @param file The path to the config.
 * @param saveAfterLoad If true, the loaded config will be saved
 * immediately. This is useful, if the config structure was changed
 * and new default parameters were applied.
 * @param default Optional default config, which will be
 * used if there is no config file and a new one should
 * be created.
 * @throws java.io.FileNotFoundException If the file does not
 * exist and no default config is specified.
 */
inline fun <reified T : Any> jsonConfig(
    file: File,
    saveAfterLoad: Boolean = false,
    noinline default: (() -> T)? = null
) = ConfigDelegate(T::class, file, saveAfterLoad, default)

/**
 * @see jsonConfig
 */
class ConfigDelegate<T : Any>(
    private val configClass: KClass<T>,
    private val file: File,
    private val saveAfterLoad: Boolean,
    private val defaultCallback: (() -> T)?
) {

    private var internalConfig: T = loadIt()

    var data: T
        get() = internalConfig
        set(value) {
            internalConfig = value
        }

    operator fun getValue(thisRef: Any?, property: KProperty<*>) = internalConfig

    operator fun setValue(thisRef: Any?, property: KProperty<*>, config: T): Boolean {
        internalConfig = config
        return true
    }

    /**
     * Saves the config object in its current state to disk.
     */
    fun save() = saveIt(internalConfig)

    /**
     * Loads the current state of the config on disk to the config object.
     */
    fun reload() {
        loadIt()
    }

    private fun saveIt(toSave: T) {
        GsonConfigManager.saveConfig(file, toSave, true)
        internalConfig = toSave
    }

    private fun loadIt(): T {

        val loaded = if (defaultCallback == null)
            GsonConfigManager.loadConfig(file, configClass.java)
        else
            GsonConfigManager.loadOrCreateDefault(file, configClass.java, true, defaultCallback)


        if (saveAfterLoad)
            saveIt(loaded)

        return loaded

    }

}