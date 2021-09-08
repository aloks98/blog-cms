package dev.aloks.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.mongodb.MongoException
import dev.aloks.ZeusConfig

import dev.aloks.interfaces.UserRepository
import dev.aloks.models.ServiceFunctionResponse
import dev.aloks.models.User
import dev.aloks.models.UserLoginResponse
import dev.aloks.models.UserRequest
import dev.aloks.plugins.BadRequestException
import dev.aloks.plugins.InternalServerExceptionWithError
import dev.aloks.plugins.NotFoundException
import dev.aloks.plugins.UnauthorizedException
import dev.aloks.repository.selectUserCollection
import org.bson.types.ObjectId
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.springframework.security.crypto.bcrypt.BCrypt
import java.lang.Exception
import java.util.*

class UserService: UserRepository {
    private val userCollection = selectUserCollection()

    override fun register(user: UserRequest): ServiceFunctionResponse {
        val (first_name, last_name, email, username, password) = user
        userCollection.findOne(User::email eq email) ?.let { throw BadRequestException("Email already exists.") }
        userCollection.findOne(User::username eq username) ?.let { throw BadRequestException("Username already exists.") }
        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
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
            throw InternalServerExceptionWithError(cause = e)
        }

    }

    override fun login(uid: String, password: String): ServiceFunctionResponse {
        try {
            val user = userCollection.findOne(User::email eq uid)
                ?: throw NotFoundException("Email not found. Please provide a valid email address.")
            val passwordCheck = BCrypt.checkpw(password, user.password)
            if (!passwordCheck) { throw UnauthorizedException("Incorrect password. Please provide correct password. ") }
            val token = JWT.create()
                .withAudience(ZeusConfig.getJwtAudience())
                .withIssuer(ZeusConfig.getJwtIssuer())
                .withClaim("username", user.username)
                .withExpiresAt(Date(System.currentTimeMillis() + 60000))
                .sign(Algorithm.HMAC256(ZeusConfig.getJwtSecret()))
            return ServiceFunctionResponse(true, "Login Successfull", data = UserLoginResponse(user.username, token))
        } catch (e: Throwable) {
            throw InternalServerExceptionWithError(cause = e)
        }
    }

    override fun resetPassword(uid: String) {
        TODO("Not yet implemented")
    }

    override fun updatePassword() {
        TODO("Not yet implemented")
    }
}

