package net.axay.blueutils.database

import com.mongodb.MongoCredential
import com.mongodb.ServerAddress

data class DatabaseLoginInformation(
    val host: String,
    val port: Int,
    val database: String,
    val user: String,
    val password: String
) {

    val mongoCredential: MongoCredential
        get() = MongoCredential.createCredential(user, database, password.toCharArray())

    val mongoServerAddress
        get() = ServerAddress(host, port)

    val isNotsetDefault
        get() = this == NOTSET_DEFAULT

    companion object {
        val NOTSET_DEFAULT get() = DatabaseLoginInformation("notset", 12345, "notset", "notset", "notset")
    }

}