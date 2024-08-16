package caddy

import caddy.util.Logger
import io.ktor.client.*
import io.ktor.client.plugins.logging.*

val LoggedHttpClient = HttpClient {
    install(Logging) {
        val httpLogger = Logger("HTTP")
        level = LogLevel.INFO
        logger = object : io.ktor.client.plugins.logging.Logger {
            override fun log(message: String) {
                httpLogger.debug(message)
            }
        }
    }
}