package dev.aloks.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.security.crypto.bcrypt.BCrypt
import org.bson.types.ObjectId
import org.litote.kmongo.*


import com.sendgrid.Method
import com.sendgrid.Request
import com.sendgrid.SendGrid
import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.*

import dev.aloks.ZeusConfig
import dev.aloks.interfaces.UserRepository
import dev.aloks.models.*
import dev.aloks.plugins.*
import dev.aloks.repository.selectTokenCollection
import dev.aloks.repository.selectUserCollection
import org.apache.commons.codec.digest.DigestUtils

import java.io.IOException
import java.lang.Exception
import java.util.*

class UserService: UserRepository {
    private val userCollection = selectUserCollection()
    private val tokenCollection = selectTokenCollection()

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
        userCollection.insertOne(newUser)
        return ServiceFunctionResponse(true, "User successfully created")
    }

    override fun login(uid: String, password: String): ServiceFunctionResponse {
        val user = userCollection.findOne(User::email eq uid)
            ?: throw NotFoundException("Email not found. Please provide a valid email address.")
        val passwordCheck = BCrypt.checkpw(password, user.password)
        if (!passwordCheck) { throw UnauthorizedException("Incorrect password. Please provide correct password.") }
        val token = JWT.create()
            .withAudience(ZeusConfig.getJwtAudience())
            .withIssuer(ZeusConfig.getJwtIssuer())
            .withClaim("username", user.username)
            .withClaim("type", TokenType.TOKEN_ACCESS.toString())
            .withIssuedAt((Date(System.currentTimeMillis())))
            .withExpiresAt(Date(System.currentTimeMillis() + 60000))
            .sign(Algorithm.HMAC256(ZeusConfig.getJwtSecret()))
        return ServiceFunctionResponse(true, "Login Successfull", data = UserLoginResponse(user.username, token))
    }

    override fun generateResetPasswordLink(uid: String) {
        val user = userCollection.findOne(User::email eq uid)
            ?: throw NotFoundException("Email not found. Please provide a valid email address.")
        val token = JWT.create()
            .withAudience(ZeusConfig.getJwtAudience())
            .withIssuer(ZeusConfig.getJwtIssuer())
            .withClaim("username", user.username)
            .withClaim("type", TokenType.TOKEN_RESET_PASSWORD.toString())
            .withIssuedAt((Date(System.currentTimeMillis())))
            .withExpiresAt(Date(System.currentTimeMillis() + 86400000))
            .sign(Algorithm.HMAC256(ZeusConfig.getJwtSecret()))
        val sha1 = DigestUtils.sha1Hex(token)
        val newToken = Token(ObjectId(), token, sha1)
        tokenCollection.insertOne(newToken)

        val appDomain = ZeusConfig.getAppDomain()

        val from = Email("no-reply@aloks.dev")
        val to = Email(user.email)
        val subject = "Password help is here"
        val content = Content()
        content.type = "text/html"
        content.value = "<p>Please <a href='${appDomain}/user/password/reset?t=${sha1}'>click here</a> to reset your password.</p>"
        val mail = Mail(from, subject, to, content)

        val sg = SendGrid(ZeusConfig.getSendgridApiKey())
        val request = Request()
        request.method = Method.POST
        request.endpoint = "mail/send"
        request.body = mail.build()
        sg.api(request)
    }

    override fun verifyResetPasswordToken(t: String): ServiceFunctionResponse {
        val dbToken = tokenCollection.findOne(Token::sha1 eq t)
            ?: throw NotFoundException("Invalid URL or Link already has been used.")
        val verifier = JWT.require(Algorithm.HMAC256(ZeusConfig.getJwtSecret()))
            .withIssuer(ZeusConfig.getJwtIssuer())
            .withClaim("type", TokenType.TOKEN_RESET_PASSWORD.toString())
            .build()
        return try {
            verifier.verify(dbToken.token)
            ServiceFunctionResponse(true, "Token verified")
        } catch (ex: Exception) {
            ServiceFunctionResponse(false, "Token Exception", ex)
        }
    }
    override fun resetPassword(t: String, password: String): ServiceFunctionResponse {
        val dbToken = tokenCollection.findOne(Token::sha1 eq t) ?: throw NotFoundException("Invalid URL or Link already has been used.")
        return try {
            val verifier = JWT.require(Algorithm.HMAC256(ZeusConfig.getJwtSecret()))
                .withIssuer(ZeusConfig.getJwtIssuer())
                .withClaim("type", TokenType.TOKEN_RESET_PASSWORD.toString())
                .build()
            verifier.verify(dbToken.token)

            val decoded = JWT.decode(dbToken.token)
            val claims = decoded.claims
            val uid = claims["username"].toString()

            val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())

            userCollection.updateOne(User::username eq uid, setValue(User::password, hashedPassword))
            tokenCollection.deleteOne(Token::sha1 eq t)
            ServiceFunctionResponse(true, "Password Successfully Updated")
        } catch (ex: Exception) {
            ServiceFunctionResponse(false, "Function Exception", ex)
        }
    }
}

