package dev.aloks

import com.typesafe.config.ConfigFactory
import io.ktor.config.*

class ZeusConfig {
    companion object {
        private val config = HoconApplicationConfig(ConfigFactory.load())

        private fun getConfig(): HoconApplicationConfig {
            return config
        }

        fun getJwtSecret(): String {
            return getConfig().property("zeus.jwt.secret").getString()
        }

        fun getJwtIssuer(): String {
            return getConfig().property("zeus.jwt.issuer").getString()
        }

        fun getJwtAudience(): String {
            return getConfig().property("zeus.jwt.audience").getString()
        }

        fun getDomain(): String {
            return getConfig().property("zeus.domain").toString()
        }

        fun isDev(): Boolean {
            return getConfig().property("ktor.development").getString() == "true"
        }

        fun getMongoUri(): String {
            return getConfig().property("db.mongo.uri").getString()
        }

        fun getDbName(): String {
            return getConfig().property("db.mongo.db_name").getString()
        }
    }
}