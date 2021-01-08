@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package net.axay.blueutils.database.mongodb

import com.mongodb.MongoClientSettings
import net.axay.blueutils.database.DatabaseLoginInformation
import org.bson.UuidRepresentation
import java.io.Closeable

abstract class MongoDB<TClient, TDatabase>(
    private val loginInformation: DatabaseLoginInformation,
    kMongo: Boolean,
    spigot: Boolean,
    clientCreator: (clientSettings: MongoClientSettings, ifKMongo: Boolean) -> TClient,
    databaseCreator: (client: TClient, databaseName: String) -> TDatabase
) : Closeable {

    val mongoClient: TClient
    val database: TDatabase

    init {

        if (spigot && kMongo) {
            System.setProperty(
                "org.litote.mongo.test.mapping.service",
                "org.litote.kmongo.jackson.JacksonClassMappingTypeService"
            )
            System.setProperty(
                "org.litote.mongo.test.mapping.service",
                "org.litote.kmongo.serialization.SerializationClassMappingTypeService"
            )
        }

        val clientSettings = MongoClientSettings.builder()
            .applyToClusterSettings { builder ->
                builder.hosts(listOf(loginInformation.mongoServerAddress))
            }
            .apply {
                loginInformation.mongoCredential?.let { this.credential(it) }
            }
            .uuidRepresentation(UuidRepresentation.STANDARD)
            .build()

        mongoClient = clientCreator(clientSettings, kMongo)

        database = databaseCreator(
            mongoClient,
            loginInformation.database
                ?: throw IllegalStateException("The database name cannot be null, when creating a MongoDB instance")
        )

    }

    /**
     * Creates a new collection.
     *
     * @param name the name of the new collection
     * @return true, if a new collection was created
     */
    abstract fun createCollection(name: String): Boolean

}