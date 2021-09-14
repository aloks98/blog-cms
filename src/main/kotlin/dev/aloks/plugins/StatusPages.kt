package dev.aloks.plugins

import com.auth0.jwt.exceptions.TokenExpiredException
import dev.aloks.models.ErrorResponse
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import java.lang.RuntimeException

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Throwable> { cause ->
            when(cause) {
                is InternalServerException -> {
                    call.response.status(HttpStatusCode.InternalServerError)
                    call.respond(ErrorResponse(500, cause.message, cause.stackTraceToString()))
                }
                is BadRequestException -> {
                    call.response.status(HttpStatusCode.BadRequest)
                    call.respond(ErrorResponse(400, cause.message, cause.stackTraceToString()))
                }
                is ForbiddenException -> {
                    call.response.status(HttpStatusCode.Forbidden)
                    call.respond(ErrorResponse(403, cause.message, cause.stackTraceToString()))
                }
                is UnauthorizedException -> {
                    call.response.status(HttpStatusCode.Unauthorized)
                    call.respond(ErrorResponse(401, cause.message, cause.stackTraceToString()))
                }
                is NotFoundException -> {
                    call.response.status(HttpStatusCode.NotFound)
                    call.respond(ErrorResponse(404, cause.message, cause.stackTraceToString()))
                }
                else -> {
                    val trace = cause.cause?.stackTraceToString() ?: cause.stackTraceToString()
                    val message = cause.cause?.message ?: cause.message
                    call.response.status(HttpStatusCode.InternalServerError)
                    call.respond(ErrorResponse(500, message, trace))
                }
            }
        }
    }

}

class InternalServerException(
    override val message: String = "Something bad happened.",
): RuntimeException()

class BadRequestException (
    override val message: String = "Bad Request. Please check the fields."
) : RuntimeException()

class ForbiddenException (
    override val message: String = "You are not allowed to access this route."
): RuntimeException()

class UnauthorizedException (
    override val message: String = "Please authorize to access this route."
): RuntimeException()

class NotFoundException (
    override val message: String = "This route does not exist."
): RuntimeException()