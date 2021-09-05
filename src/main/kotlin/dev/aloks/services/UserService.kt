package dev.aloks.services

import at.favre.lib.crypto.bcrypt.BCrypt
import com.mongodb.MongoException

import dev.aloks.interfaces.UserRepository
import dev.aloks.models.ServiceFunctionResponse
import dev.aloks.models.User
import dev.aloks.models.UserRequest
import dev.aloks.plugins.BadRequestException
import dev.aloks.plugins.InternalServerException
import dev.aloks.repository.selectUserCollection
import org.bson.types.ObjectId
import org.litote.kmongo.eq
import org.litote.kmongo.findOne

class UserService: UserRepository {
    private val userCollection = selectUserCollection()

    override fun register(user: UserRequest): ServiceFunctionResponse {
        val (first_name, last_name, email, username, password) = user
        userCollection.findOne(User::email eq email) ?.let { throw BadRequestException("Email already exists.") }
        userCollection.findOne(User::username eq username) ?.let { throw BadRequestException("Username already exists.") }
        val hashedPassword = BCrypt.with(BCrypt.Version.VERSION_2B).hashToString(10, password.toCharArray())
        val newUser = User (
            _id = ObjectId(),
            first_name = first_name,
            last_name = last_name,
            email = email,
            username = username,
            password = hashedPassword,
        )
        return try {
            userCollection.insertOne(newUser)
            ServiceFunctionResponse(true, "User successfully created")
        } catch (e: MongoException) {
            throw InternalServerException(cause = e)
        }

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

