package net.axay.blueutils.files

import java.io.File

/**
 * Creates this file, aswell as all needed parent directories.
 *
 * @return true, if a new file was created
 */
fun File.createIfNotExists(): Boolean {
    return if (!exists()) {
        if (isDirectory)
            mkdirs()
        else {
            val absolute = absoluteFile
            if (!absolute.parentFile.exists())
                absolute.parentFile.mkdirs()
            createNewFile()
        }
    } else true
}