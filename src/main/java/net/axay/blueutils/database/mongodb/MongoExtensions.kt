package net.axay.blueutils.database.mongodb

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.CountOptions
import org.bson.conversions.Bson

fun <T> MongoCollection<T>.contains(filter: Bson)
        = countDocuments(filter, CountOptions().limit(1)) == 1L
