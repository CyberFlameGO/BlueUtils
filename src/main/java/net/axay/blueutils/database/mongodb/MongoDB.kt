@file:Suppress("MemberVisibilityCanBePrivate")

package net.axay.blueutils.database.mongodb

import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.mongodb.client.ClientSession
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import net.axay.blueutils.database.DatabaseLoginInformation
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection
import java.lang.IllegalStateException

class MongoDB(databaseLoginInformation: DatabaseLoginInformation): AutoCloseable {

    val database: MongoDatabase?

    private var _mongoClient: MongoClient? = null
    val mongoClient: MongoClient get() = _mongoClient ?: throw IllegalStateException("Trying to access MongoClient while it is null")

    init {

        databaseLoginInformation.let {

            val mongoClient = KMongo.createClient(

                MongoClientSettings.builder()

                    .applyToClusterSettings { builder ->
                        builder.hosts(listOf(ServerAddress(it.host, it.port)))
                    }
                    .credential(MongoCredential.createCredential(it.user, it.database, it.password.toCharArray()))

                    .build()

            )

            database = try {
                this._mongoClient = mongoClient
                mongoClient.getDatabase(it.database)
            } catch (exc: Exception) {
                exc.printStackTrace()
                null
            }

        }

    }

    inline fun <reified T : Any> getCollection(
            name: String,
            noinline onCreate: ((MongoCollection<T>) -> Unit)? = null
    ): MongoCollection<T>? {
        database?.let {

            var ifNew = false
            if (!it.listCollectionNames().contains(name)) {
                it.createCollection(name)
                ifNew = true
            }

            val collection = it.getCollection<T>(name)
            if (ifNew)
                onCreate?.invoke(collection)

            return collection
        }
        return null
    }

    fun <T> executeTransaction(transaction: (ClientSession) -> T): T {
        val clientSession = mongoClient.startSession()
        val result = clientSession.withTransaction { transaction.invoke(clientSession) }
        clientSession.close()
        return result
    }

    override fun close() {
        this.mongoClient.close()
    }

}