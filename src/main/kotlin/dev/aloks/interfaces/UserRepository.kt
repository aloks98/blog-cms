package dev.aloks.interfaces

import dev.aloks.models.ServiceFunctionResponse
import dev.aloks.models.UserRequest
import dev.aloks.models.UserUpdateRequest

interface UserRepository {
    fun register(user: UserRequest): ServiceFunctionResponse
    fun login(uid: String, password: String): ServiceFunctionResponse
    fun generateResetPasswordLink(uid: String)
    fun verifyResetPasswordToken(t: String): ServiceFunctionResponse
    fun resetPassword(t: String, password: String): ServiceFunctionResponse
    fun generateMagicLink(uid: String)
    fun magicLinkLogin(t: String): ServiceFunctionResponse
    fun getSelfProfile(username: String): ServiceFunctionResponse
    fun updateSelfProfile(username: String, user: UserUpdateRequest): ServiceFunctionResponse
}