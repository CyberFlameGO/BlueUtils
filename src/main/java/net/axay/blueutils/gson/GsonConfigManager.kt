@file:Suppress("MemberVisibilityCanBePrivate")

package net.axay.blueutils.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import net.axay.blueutils.files.createIfNotExists
import java.io.File
import java.io.FileReader
import java.io.FileWriter

fun gson(pretty: Boolean = true) = if (pretty) GsonInstanceHolder.gsonPretty else GsonInstanceHolder.gson

object GsonInstanceHolder {

    private val gsonBuilder by lazy {
        GsonBuilder()
    }

    val gson: Gson by lazy { gsonBuilder.create() }
    val gsonPretty: Gson by lazy { gsonBuilder.setPrettyPrinting().create() }

}

object GsonConfigManager {

    fun <T> saveConfig(file: File, defaultConfig: T, pretty: Boolean = true) {
        file.createIfNotExists()
        FileWriter(file).use { writer ->
            gson(true).toJson(defaultConfig, writer)
        }
    }

    fun <T> loadConfig(file: File, configClass: Class<T>): T
            = FileReader(file).use { reader -> return gson(false).fromJson(reader, configClass) }

    fun <T> loadOrCreateDefault(
            file: File,
            configClass: Class<T>,
            pretty: Boolean = true,
            default: () -> T
    ): T {
        try {
            return loadConfig(file, configClass)
        } catch (exc: Exception) {
            default.invoke().let {
                saveConfig(file, it, pretty)
                return it
            }
        }
    }

}