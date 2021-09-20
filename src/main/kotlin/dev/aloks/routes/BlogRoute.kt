package dev.aloks.routes

import dev.aloks.models.BlogRequest
import dev.aloks.services.BlogService
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.blogs() {
    val blogService = BlogService()
    route("/") {
        authenticate("jwt") {
            post("create") {

            }
            patch("{slug}/edit") {

            }
            delete("{slug}/delete") {

            }
            get("user") {

            }
        }
        get("all") {

        }
        get("slug/{slug}") {

        }
        get("id/{bid}") {

        }
    }
}
