package dev.aloks.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import java.time.LocalDateTime

@Serializable
data class Blog(
    @Contextual val _id: ObjectId = ObjectId(),
    val slug: String,
    val series: String? = null,
    val introduction: String,
    val content: String,
    val published: Boolean? = true,
    val featured: Boolean? = false,
    @Contextual val created_by: ObjectId,
    @Contextual val created_at: LocalDateTime? = LocalDateTime.now(),
    @Contextual val updated_at: LocalDateTime? = LocalDateTime.now()
)

@Serializable
data class BlogRequest(
    val slug: String,
    val series: String? = null,
    val introduction: String,
    val content: String,
    val published: Boolean? = true,
    val featured: Boolean? = false,
)

@Serializable
data class BlogUpdateRequest(
    val series: String? = null,
    val introduction: String? = null,
    val content: String? = null,
    val published: Boolean? = null,
    val featured: Boolean? = null,
)

@Serializable
data class BlogCreatedBy(
    val id: String,
    val name: String,
    val username: String
)

@Serializable
data class BlogResponse(
    val id: String,
    val slug: String,
    val series: String,
    val introduction: String,
    val content: String,
    val published: Boolean,
    val createdBy: BlogCreatedBy,
    @Contextual val updated_at: LocalDateTime
)