package dev.aloks.routes

import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.TokenExpiredException
import dev.aloks.models.*
import dev.aloks.plugins.*
import dev.aloks.services.UserService
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.lang.Exception

fun Route.user() {
    val userService = UserService()
    route("/") {
        post("register") {
            val user = call.receive<UserRequest>()
            val res = userService.register(user)
            call.response.status(HttpStatusCode.Created)
            call.respond(SuccessfulResponse(201, res.message))
        }
        route("login") {
            post("") {
                val (uid, password) = call.receive<LoginRequest>()
                val login = userService.login(uid, password)
                call.response.status(HttpStatusCode.OK)
                call.respond(SuccessfulLoginResponse(data = login.data as UserLoginResponse))
            }
            post("/generate_magic_link") {
                val uid = call.parameters["uid"] ?: throw BadRequestException("Email is required.")
                userService.generateMagicLink(uid)
                call.response.status(HttpStatusCode.OK)
                call.respond(SuccessfulResponse(message = "Expect a Magic signin link in your inbox."))
            }
            post("/magic") {
                val uid = call.parameters["t"] ?: throw BadRequestException("Token is required.")
                val res = userService.magicLinkLogin(uid)
                when (res.success) {
                    true -> {
                        call.response.status(HttpStatusCode.OK)
                        call.respond(SuccessfulLoginResponse(data = res.data as UserLoginResponse))
                    }
                    false -> {
                        when (res.error) {
                            is TokenExpiredException -> {
                                throw UnauthorizedException("Link Expired. Please generate a new link.")
                            }
                            else -> {
                                throw res.error!!
                            }
                        }
                    }
                }
            }
        }
        route("pw/") {
            post("reset_email") {
                val uid = call.parameters["uid"] ?: throw BadRequestException("Email is required.")
                userService.generateResetPasswordLink(uid)
                call.response.status(HttpStatusCode.OK)
                call.respond(SuccessfulResponse(message = "Password reset email sent successfully"))
            }
            post("verify_reset_token") {
                val token = call.parameters["t"] ?: throw BadRequestException("Token is missing")
                val res = userService.verifyResetPasswordToken(token)
                when (res.success) {
                    true -> {
                        call.response.status(HttpStatusCode.OK)
                        call.respond(SuccessfulResponse(message = "Token Verified"))
                    }
                    false -> {
                        when (res.error) {
                            is TokenExpiredException -> {
                                throw UnauthorizedException("Link Expired. Please generate a new link.")
                            }
                            else -> {
                                throw res.error!!
                            }
                        }
                    }
                }
            }
            post("reset") {
                val token = call.parameters["t"] ?: throw BadRequestException("Token is missing from reset request.")
                val body = call.receive<PasswordResetRequest>()
                val res = userService.resetPassword(token, body.password)
                when (res.success) {
                    true -> {
                        call.response.status(HttpStatusCode.OK)
                        call.respond(SuccessfulResponse(message = "Password Successfully Updated"))
                    }
                    false -> {
                        when (res.error) {
                            is TokenExpiredException -> {
                                throw UnauthorizedException("Link Expired during password update.")
                            }
                            is JWTDecodeException -> {
                                throw InternalServerException("JWT Cannot be decoded.")
                            }
                            else -> {
                                throw res.error!!
                            }
                        }
                    }
                }
            }
        }
        authenticate("jwt") {
            route("self") {
                get("") {
                    val principal = call.principal<JWTPrincipal>()
                    val username = principal!!.payload.getClaim("username").asString()
                    val res = userService.getSelfProfile(username)
                    call.response.status(HttpStatusCode.OK)
                    call.respond(SuccessfulUserFetchResponse(data = res.data as UserResponse))
                }
                post("/update") {
                    val principal = call.principal<JWTPrincipal>()
                    val username = principal!!.payload.getClaim("username").asString()
                    val user = call.receive<UserUpdateRequest>()
                    val res = userService.updateSelfProfile(username, user)
                    call.response.status(HttpStatusCode.OK)
                    call.respond(SuccessfulResponse(200, res.message))
                }
            }
        }
    }
}
