package net.axay.blueutils.files

import java.io.File

fun File.createIfNotExists(): Boolean {
    return if (!exists()) {
        if (!parentFile.exists())
            parentFile.mkdirs()
        createNewFile()
    } else true
}