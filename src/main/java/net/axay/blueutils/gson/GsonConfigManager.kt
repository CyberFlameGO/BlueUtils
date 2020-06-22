package net.axay.blueutils.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import net.axay.blueutils.files.createIfNotExists
import java.io.File
import java.io.FileReader
import java.io.FileWriter

object GsonConfigManager {

    private val gson = Gson()
    private val gsonPretty = GsonBuilder()
            .setPrettyPrinting()
            .create()

    fun <T> loadConfig(file: File, configClass: Class<T>): T
            = FileReader(file).use { reader -> return gson.fromJson(reader, configClass) }

    fun <T> saveConfig(file: File, defaultConfig: T, pretty: Boolean = true) {
        file.createIfNotExists()
        FileWriter(file).use { writer ->
            val thisGson: Gson = if (pretty) gsonPretty else gson
            thisGson.toJson(defaultConfig, writer)
        }
    }

}