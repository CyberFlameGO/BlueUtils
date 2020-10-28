@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package net.axay.blueutils.database.mongodb

import com.mongodb.MongoClientSettings
import com.mongodb.client.*
import net.axay.blueutils.database.DatabaseLoginInformation
import org.bson.Document
import org.bson.UuidRepresentation
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection
import java.io.Closeable

class MongoDB(
        private val loginInformation: DatabaseLoginInformation,
        kMongo: Boolean = true,
        spigot: Boolean = false
): Closeable {

    val mongoClient: MongoClient
    val database: MongoDatabase

    init {

        if (spigot && kMongo)  {
            System.setProperty("org.litote.mongo.test.mapping.service", "org.litote.kmongo.jackson.JacksonClassMappingTypeService")
            System.setProperty("org.litote.mongo.test.mapping.service", "org.litote.kmongo.serialization.SerializationClassMappingTypeService")
        }

        val clientSettings = MongoClientSettings.builder()
            .applyToClusterSettings { builder ->
                builder.hosts(listOf(loginInformation.mongoServerAddress))
            }
            .credential(loginInformation.mongoCredential)
            .uuidRepresentation(UuidRepresentation.STANDARD)
        .build()

        mongoClient = if (kMongo) KMongo.createClient(clientSettings) else MongoClients.create(clientSettings)

        database = mongoClient.getDatabase(loginInformation.database)

    }

    inline fun <reified T : Any> getCollectionOrCreate(
            name: String,
            noinline onCreate: ((MongoCollection<T>) -> Unit)? = null
    ): MongoCollection<T> {

        var ifNew = false
        if (!database.listCollectionNames().contains(name)) {
            database.createCollection(name)
            ifNew = true
        }

        val collection = database.getCollection<T>(name)
        if (ifNew)
            onCreate?.invoke(collection)

        return collection

    }

    fun getDocumentCollectionOrCreate(
            name: String,
            onCreate: ((MongoCollection<Document>) -> Unit)? = null
    ) = getCollectionOrCreate(name, onCreate)

    fun <T> executeTransaction(transaction: (ClientSession) -> T): T
        = mongoClient.startSession().use { session ->
            session.withTransaction { transaction.invoke(session) }
        }

    override fun close() {
        this.mongoClient.close()
    }

}