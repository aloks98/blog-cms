package dev.aloks.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class User (
    @Contextual val _id: ObjectId = ObjectId(),
    val first_name: String,
    val last_name: String,
    val email: String,
    val username: String,
    val password: String,
    val blogs: MutableSet<String>? = mutableSetOf()
)

@Serializable
data class UserRequest (
    val first_name: String,
    val last_name: String,
    val email: String,
    val username: String,
    val password: String,
)

@Serializable
data class UserUpdateRequest (
    val first_name: String? = null,
    val last_name: String? = null,
    val email: String? = null,
)


@Serializable
data class UserResponse (
    val id: String,
    val name: String,
    val email: String,
    val username: String,
    val blogs: MutableSet<String>? = null
)

@Serializable
data class LoginRequest (
    val uid: String,
    val password: String
)

@Serializable
data class PasswordResetRequest (
    val password: String
        )

@Serializable
data class UserLoginResponse (
    val username: String,
    val token: String
)