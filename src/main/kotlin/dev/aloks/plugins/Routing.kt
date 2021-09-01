package dev.aloks.plugins

import dev.aloks.routes.blogs
import dev.aloks.routes.user
import io.ktor.routing.*
import io.ktor.application.*

fun Application.configureRouting() {

    routing {
        route("/api/v1") {
            route("/blogs") {
                blogs()
            }
            route("/user") {
                user()
            }
        }

    }
}
