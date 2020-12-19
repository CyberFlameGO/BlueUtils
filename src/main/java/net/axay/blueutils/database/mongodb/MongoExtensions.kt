package net.axay.blueutils.database.mongodb

import com.mongodb.ErrorCategory
import com.mongodb.MongoWriteException
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.CountOptions
import com.mongodb.client.result.InsertOneResult
import org.bson.BsonValue
import org.bson.conversions.Bson
import org.litote.kmongo.id.WrappedObjectId

fun <T> MongoCollection<T>.contains(filter: Bson) =
    countDocuments(filter, CountOptions().limit(1)) == 1L

fun <T> MongoCollection<T>.insertOneCatchDuplicate(document: T): InsertOneResult =
    try {
        insertOne(document)
    } catch (exc: MongoWriteException) {
        if (exc.error.category == ErrorCategory.DUPLICATE_KEY) InsertOneResult.unacknowledged() else throw exc
    }

fun <T> BsonValue.asKMongoId() = WrappedObjectId<T>(asObjectId().value)