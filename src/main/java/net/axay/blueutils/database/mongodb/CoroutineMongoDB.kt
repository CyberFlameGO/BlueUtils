package net.axay.blueutils.database.mongodb

import kotlinx.coroutines.runBlocking
import net.axay.blueutils.database.DatabaseLoginInformation
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.coroutine

/**
 * An instance of this class represents a coroutine based
 * connection to a single MongoDB database.
 *
 * @param kMongo set this to true if you are working with KMongo (for kotlin)
 * @param spigot set this to true if you are working with spigot (a minecraft server api)
 */
class CoroutineMongoDB(
    loginInformation: DatabaseLoginInformation,
    kMongo: Boolean = true,
    spigot: Boolean = false,
) : MongoDB<org.litote.kmongo.coroutine.CoroutineClient, org.litote.kmongo.coroutine.CoroutineDatabase>(
    loginInformation, kMongo, spigot,

    clientCreator = { clientSettings, ifKMongo ->
        if (ifKMongo) org.litote.kmongo.reactivestreams.KMongo.createClient(clientSettings).coroutine
        else com.mongodb.reactivestreams.client.MongoClients.create(clientSettings).coroutine
    },

    databaseCreator = { client, databaseName ->
        client.getDatabase(databaseName)
    }
) {

    /**
     * Tries to get the given collection and creates a new one
     * if no collection could be found.
     *
     * @param name the name of the collection
     * @param onCreate an optional callback, which should be invoked if a new
     * collection was created
     */
    inline fun <reified T : Any> getCollectionOrCreate(
        name: String,
        noinline onCreate: ((CoroutineCollection<T>) -> Unit)? = null
    ): CoroutineCollection<T> {

        val ifNew = createCollection(name)

        val collection = database.getCollection<T>(name)
        if (ifNew) onCreate?.invoke(collection)

        return collection

    }

    /**
     * Creates a new collection.
     *
     * @param name the name of the new collection
     * @return true, if a new collection was created
     */
    override fun createCollection(name: String) = runBlocking {
        if (!database.listCollectionNames().contains(name)) {
            database.createCollection(name)
            true
        } else false
    }

    override fun close() = mongoClient.close()

}