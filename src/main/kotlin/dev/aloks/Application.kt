package dev.aloks

import io.ktor.application.*
import dev.aloks.plugins.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureAuthentication()
    configureRouting()
    configureHTTP()
    configureSerialization()
    configureStatusPages()
    configureCallLogger()
}
