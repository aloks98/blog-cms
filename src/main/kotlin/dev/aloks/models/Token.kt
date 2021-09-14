package dev.aloks.models

import kotlinx.serialization.Contextual
import org.bson.types.ObjectId


enum class TokenType {
    TOKEN_ACCESS,
    TOKEN_REFRESH,
    TOKEN_RESET_PASSWORD,
    TOKEN_MAGIC_URL
}

data class Token (
    @Contextual val _id: ObjectId = ObjectId(),
    val token: String,
    val sha1: String
)