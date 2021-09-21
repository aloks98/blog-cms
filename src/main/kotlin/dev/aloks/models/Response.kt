package dev.aloks.models

import kotlinx.serialization.Serializable
import java.lang.Exception

data class ServiceFunctionResponse (
    val success: Boolean,
    val message: String,
    val error: Exception? = null,
    val data: Any? = null
)

@Serializable
data class SuccessfulResponse (
    val status: Int? = 200,
    val message: String? = "Request Successfull",
)

@Serializable
data class ErrorResponse (
    val status: Int? = 500,
    val message: String? = "Something went Wrong",
    val error: String? = null
)

@Serializable
data class SuccessfulLoginResponse (
    val status: Int = 200,
    val message: String = "Login Successfull",
    val data: UserLoginResponse
)
@Serializable
data class SuccessfulUserFetchResponse (
    val status: Int = 200,
    val message: String = "User fetch sucessfull.",
    val data: UserResponse
)

@Serializable
data class SuccessfullAllBlogsFetchResponse (
    val status: Int = 200,
    val message: String = "All blogs fetch successfull.",
    val data: MutableList<BlogResponse>
)

