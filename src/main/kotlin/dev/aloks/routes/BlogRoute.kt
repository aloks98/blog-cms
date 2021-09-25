package dev.aloks.routes

import dev.aloks.models.*
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
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("username").asString()
                val slug = call.parameters["slug"] ?: throw BadRequestException("Please provide blog slug.")
                val blog = call.receive<BlogUpdateRequest>()
                val res = blogService.editBlog(slug, blog, username)
                call.response.status(HttpStatusCode.Created)
                call.respond(SuccessfulResponse(201, res.message))
            }
            delete("{slug}/delete") {

            }
            get("user") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("username").asString()
                val blogs = blogService.getUserBlogs(username)
                call.response.status(HttpStatusCode.OK)
                call.respond(SuccessfullMultipleBlogsFetchResponse(data = blogs))
            }
        }
        get("all") {
            val res = blogService.getAllBlogs()
            call.response.status(HttpStatusCode.OK)
            call.respond(SuccessfullMultipleBlogsFetchResponse(data = res))
        }
        get("slug") {
            val slug = call.parameters["s"] ?: throw BadRequestException("Please provide blog slug.")
            val blog = blogService.getBlogBySlug(slug)
            call.response.status(HttpStatusCode.OK)
            call.respond(SuccessfullBlogFetchResponse(data = blog))
        }
        get("id") {
            val bid = call.parameters["b"] ?: throw BadRequestException("Please provide blog ID.")
            val blog = blogService.getBlogById(bid)
            call.response.status(HttpStatusCode.OK)
            call.respond(SuccessfullBlogFetchResponse(data = blog))
        }
    }
}
