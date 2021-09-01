package dev.aloks.routes

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.blogs() {
    route("/") {
        post("create") {
            call.respondText("create blogs")
        }
    }
}
