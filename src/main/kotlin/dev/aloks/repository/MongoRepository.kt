package dev.aloks.repository

import com.mongodb.MongoClientSettings
import dev.aloks.ZeusConfig
import dev.aloks.models.Blog
import dev.aloks.models.User
import com.mongodb.client.*
import org.bson.Document
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.pojo.PojoCodecProvider

val pojoCodecRegistry = CodecRegistries.fromRegistries(
        MongoClientSettings.getDefaultCodecRegistry(),
        CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
    )

val mongoUri: String = ZeusConfig.getMongoUri()
val db = ZeusConfig.getDbName()

fun selectUserCollection(): MongoCollection<User> {
    val client = MongoClients.create(mongoUri+"/"+db)
    val database = client.getDatabase(db).withCodecRegistry(pojoCodecRegistry)
    return database.getCollection("user", User::class.java)
}

fun selectBlogCollection(): MongoCollection<Blog> {
    val client = MongoClients.create(mongoUri+"/"+db)
    val database = client.getDatabase(db).withCodecRegistry(pojoCodecRegistry)
    return database.getCollection("blog", Blog::class.java)
}
