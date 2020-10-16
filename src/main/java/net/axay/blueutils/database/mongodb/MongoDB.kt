@file:Suppress("MemberVisibilityCanBePrivate")

package net.axay.blueutils.database.mongodb

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.mongodb.client.*
import com.mongodb.connection.ServerSettings
import com.mongodb.event.ServerListener
import net.axay.blueutils.database.DatabaseLoginInformation
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection

class MongoDB(databaseLoginInformation: DatabaseLoginInformation, kMongo: Boolean = true): AutoCloseable {

    val database: MongoDatabase?

    private var _mongoClient: MongoClient? = null
    val mongoClient: MongoClient get() = _mongoClient ?: throw IllegalStateException("Trying to access MongoClient while it is null")

    init {

        databaseLoginInformation.let {

            val clientSettings = MongoClientSettings.builder()
                .applyToClusterSettings { builder ->
                    builder.hosts(listOf(it.mongoServerAddress))
                }
                .credential(it.mongoCredential)
            .build()

            _mongoClient = if (kMongo) KMongo.createClient(clientSettings) else MongoClients.create(clientSettings)

            database = try {
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