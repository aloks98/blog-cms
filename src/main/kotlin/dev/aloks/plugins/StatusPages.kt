package dev.aloks.plugins

import dev.aloks.models.ErrorResponse
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import java.lang.RuntimeException

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<InternalServerExceptionWithError> { cause ->
            call.response.status(HttpStatusCode.InternalServerError)
            call.respond(ErrorResponse(500, cause.cause.localizedMessage, cause.cause.stackTraceToString()))
        }
        exception<InternalServerException> { cause ->
            call.response.status(HttpStatusCode.InternalServerError)
            call.respond(ErrorResponse(500, cause.message, cause.stackTraceToString()))
        }
        exception<BadRequestException> { cause ->
            call.response.status(HttpStatusCode.BadRequest)
            call.respond(ErrorResponse(400, cause.message, cause.stackTraceToString()))
        }
        exception<ForbiddenException> { cause ->
            call.response.status(HttpStatusCode.Forbidden)
            call.respond(ErrorResponse(403, cause.message, cause.stackTraceToString()))
        }
        exception<UnauthorizedException> { cause ->
            call.response.status(HttpStatusCode.Unauthorized)
            call.respond(ErrorResponse(401, cause.message, cause.stackTraceToString()))
        }
        exception<NotFoundException> { cause ->
            call.response.status(HttpStatusCode.NotFound)
            call.respond(ErrorResponse(404, cause.message, cause.stackTraceToString()))
        }
    }

}

class InternalServerExceptionWithError(
    override val message: String = "Something bad happened.",
    override val cause: Throwable
): RuntimeException()

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