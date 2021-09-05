package dev.aloks.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.lang.Exception

@Serializable
data class SuccessfulResponse (
    val status: Int? = 200,
    val message: String? = "Request Successfull",
    @Contextual val data: Any? = null
)

@Serializable
data class ErrorResponse (
    val status: Int? = 500,
    val message: String,
    val error: String? = null
)

data class ServiceFunctionResponse (
    val success: Boolean,
    val message: String,
    val error: Exception? = null
)
