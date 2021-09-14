package dev.aloks.interfaces

import dev.aloks.models.ServiceFunctionResponse
import dev.aloks.models.UserLoginResponse
import dev.aloks.models.UserRequest

interface UserRepository {
    fun register(user: UserRequest): ServiceFunctionResponse
    fun login(uid: String, password: String): ServiceFunctionResponse
    fun generateResetPasswordLink(uid: String)
    fun verifyResetPasswordToken(t: String): ServiceFunctionResponse
    fun resetPassword(t: String, password: String): ServiceFunctionResponse
}