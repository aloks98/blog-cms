package dev.aloks.models

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Blog(
    val id: String,
    val series: String? = null,
    val introduction: String? = null,
    val content: String,
    val published: Boolean? = true,
    val featured: Boolean? = false,
    val created_by: UserResponse,
    val created_at: String? = LocalDateTime.now().toString(),
    val updated_at: String? = LocalDateTime.now().toString()
)