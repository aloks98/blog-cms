package dev.aloks.repository

import com.mongodb.client.MongoCollection
import dev.aloks.ZeusConfig
import dev.aloks.models.Blog
import dev.aloks.models.User
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection

val mongoUri: String = ZeusConfig.getMongoUri()
val db = ZeusConfig.getDbName()

fun selectUserCollection(): MongoCollection<User> {
    val client = KMongo.createClient("$mongoUri/$db")
    val database = client.getDatabase(db)
    return database.getCollection<User>("users")
}

fun selectBlogCollection(): MongoCollection<Blog> {
    val client = KMongo.createClient("$mongoUri/$db")
    val database = client.getDatabase(db)
    return database.getCollection<Blog>("blogs")
}
