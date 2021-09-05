package dev.aloks.routes

import dev.aloks.models.LoginRequest
import dev.aloks.models.SuccessfulResponse
import dev.aloks.models.UserRequest
import dev.aloks.plugins.BadRequestException
import dev.aloks.services.UserService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.user() {
    val userService = UserService()
    route("/") {
        post("register") {
            val user = call.receive<UserRequest>()
            val res = userService.register(user)
            call.response.status(HttpStatusCode.Created)
            call.respond(SuccessfulResponse(201, res.message))
        }
        post("login") {
            val (uid, password) = call.receive<LoginRequest>()
            userService.login(uid, password)
        }

    }
}
