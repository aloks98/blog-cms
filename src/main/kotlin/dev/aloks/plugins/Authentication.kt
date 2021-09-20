package dev.aloks.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.aloks.ZeusConfig
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*

fun Application.configureAuthentication() {
    install(Authentication) {
        jwt("jwt") {
            verifier(JWT
                .require(Algorithm.HMAC256(ZeusConfig.getJwtSecret()))
                .withAudience(ZeusConfig.getJwtAudience())
                .withIssuer(ZeusConfig.getJwtIssuer())
                .build())

            validate { credential ->
                if (credential.payload.getClaim("username").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    throw UnauthorizedException()
                }
            }

            challenge { _, _ -> throw UnauthorizedException() }
        }
    }
}
