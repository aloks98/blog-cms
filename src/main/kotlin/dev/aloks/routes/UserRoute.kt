package dev.aloks.routes

import dev.aloks.models.UserRequest
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.user() {
    route("/") {
        post("register") {
            val requestBody = call.receive<UserRequest>()
            println("Received $requestBody")
            call.respond(requestBody)
        }
    }
}
