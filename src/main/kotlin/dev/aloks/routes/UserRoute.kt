package dev.aloks.routes

import dev.aloks.models.LoginRequest
import dev.aloks.models.UserRequest
import dev.aloks.services.UserService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.user() {
    route("/") {
        val UserService = UserService()
        post("register") {
            val (first_name, last_name, email, username, password) = call.receive<UserRequest>()
            UserService.register(first_name, last_name, email, username, password)
            call.response.status(HttpStatusCode.Created)
            call.respond("requestBody")
        }
        post("login") {
            val (uid, password) = call.receive<LoginRequest>()
            UserService.login(uid, password)
        }

    }
}
