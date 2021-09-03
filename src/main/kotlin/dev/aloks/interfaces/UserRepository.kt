package dev.aloks.interfaces

import dev.aloks.models.UserRequest

interface UserRepository {
    fun register(first_name: String, last_name: String, email: String, username: String, password: String)
    fun login(uid: String, password: String)
    fun resetPassword(uid: String)
    fun updatePassword()
}