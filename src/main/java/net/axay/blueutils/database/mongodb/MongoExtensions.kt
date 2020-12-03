package net.axay.blueutils.database.mongodb

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.CountOptions
import org.bson.BsonValue
import org.bson.conversions.Bson
import org.litote.kmongo.id.WrappedObjectId

fun <T> MongoCollection<T>.contains(filter: Bson) =
    countDocuments(filter, CountOptions().limit(1)) == 1L

fun <T> BsonValue.asKMongoId() = WrappedObjectId<T>(asObjectId().value)