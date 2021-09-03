package dev.aloks.services

import at.favre.lib.crypto.bcrypt.BCrypt
import dev.aloks.interfaces.UserRepository
import dev.aloks.models.User
import dev.aloks.repository.selectUserCollection
import org.bson.types.ObjectId

class UserService: UserRepository {
    private val userCollection = selectUserCollection()

    override fun register(first_name: String, last_name: String, email: String, username: String, password: String) {
        val hashedPassword = BCrypt.with(BCrypt.Version.VERSION_2B).hashToString(10, password.toCharArray())
        val newUser = User (
            _id = ObjectId(),
            first_name = first_name,
            last_name = last_name,
            email = email,
            username = username,
            password = hashedPassword,
            blogs = null
        )
        val status = userCollection.insertOne(newUser)
        println(status)
    }

    override fun login(uid: String, password: String) {
    }

    override fun resetPassword(uid: String) {
        TODO("Not yet implemented")
    }

    override fun updatePassword() {
        TODO("Not yet implemented")
    }
}

