package dev.aloks.routes

import dev.aloks.models.BlogRequest
import dev.aloks.models.SuccessfulResponse
import dev.aloks.models.SuccessfullAllBlogsFetchResponse
import dev.aloks.models.SuccessfullBlogFetchResponse
import dev.aloks.plugins.BadRequestException
import dev.aloks.services.BlogService
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.blogs() {
    val blogService = BlogService()
    route("/") {
        authenticate("jwt") {
            post("create") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("username").asString()
                val blog = call.receive<BlogRequest>()
                val res = blogService.createBlog(blog, username)
                call.response.status(HttpStatusCode.Created)
                call.respond(SuccessfulResponse(201, res.message))
            }
            patch("{slug}/edit") {

            }
            delete("{slug}/delete") {

            }
            get("user") {

            }
        }
        get("all") {
            val res = blogService.getAllBlogs()
            call.response.status(HttpStatusCode.OK)
            call.respond(SuccessfullAllBlogsFetchResponse(data = res))
        }
        get("slug/{slug}") {

        }
        get("id") {
            val bid = call.parameters["b"] ?: throw BadRequestException("Please provide blog ID.")
            val blog = blogService.getBlogById(bid)
            call.response.status(HttpStatusCode.OK)
            call.respond(SuccessfullBlogFetchResponse(data = blog))
        }
    }
}
