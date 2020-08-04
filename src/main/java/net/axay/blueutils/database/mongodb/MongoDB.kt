package net.axay.blueutils.database.mongodb

import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import net.axay.blueutils.database.DatabaseLoginInformation
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection

class MongoDB(databaseLoginInformation: DatabaseLoginInformation): AutoCloseable {

    val database: MongoDatabase?

    private var mongoClient: MongoClient? = null

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
                this.mongoClient = mongoClient
                mongoClient.getDatabase(it.database)
            } catch (exc: Exception) {
                exc.printStackTrace()
                null
            }

        }

    }

    inline fun <reified T : Any> getCollection(name: String): MongoCollection<T>? {
        database?.let {
            if (!it.listCollectionNames().contains(name))
                it.createCollection(name)
            return it.getCollection<T>(name)
        }
        return null
    }

    override fun close() {
        this.mongoClient?.close()
    }

}