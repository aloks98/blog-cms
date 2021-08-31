package dev.aloks.models

import kotlinx.serialization.Serializable

data class User (
    val id: String,
    val first_name: String,
    val last_name: String,
    val email: String,
    val username: String,
    val password: String,
    val blogs: MutableSet<String>
)

@Serializable
data class UserResponse (
    val id: String,
    val first_name: String,
    val last_name: String,
    val email: String,
    val username: String,
    val blogs: MutableSet<String>
)