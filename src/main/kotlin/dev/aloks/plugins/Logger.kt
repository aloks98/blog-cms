package dev.aloks.plugins

import io.ktor.application.*
import io.ktor.features.*

fun Application.configureCallLogger() {
    install(CallLogging)
}