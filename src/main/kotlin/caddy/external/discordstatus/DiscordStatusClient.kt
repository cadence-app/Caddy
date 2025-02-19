package caddy.external.discordstatus

import caddy.external.discordstatus.model.StatusPageComponentsResponse
import caddy.external.discordstatus.model.StatusPageIncidentsResponse
import caddy.util.Logger
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy

object DiscordStatusClient {

    private const val BASE_URL = "https://discordstatus.com/api/v2"

    @OptIn(ExperimentalSerializationApi::class)
    private val json = Json {
        namingStrategy = JsonNamingStrategy.SnakeCase
        ignoreUnknownKeys = true
    }

    private val httpClient = HttpClient() {
        install(ContentNegotiation) {
            json(json)
        }

        install(HttpCache)

        install(Logging) {
            val httpLogger = Logger("DiscordStatus")
            level = LogLevel.INFO
            logger = object : io.ktor.client.plugins.logging.Logger {
                override fun log(message: String) {
                    httpLogger.debug(message)
                }
            }
        }
    }

    suspend fun tryGetComponents(): StatusPageComponentsResponse? {
        return try {
            httpClient.get("$BASE_URL/components.json").body<StatusPageComponentsResponse>()
        } catch (e: Throwable) {
            null
        }
    }

    suspend fun tryGetIncidents(): StatusPageIncidentsResponse? {
        return try {
            httpClient.get("$BASE_URL/incidents.json").body<StatusPageIncidentsResponse>()
        } catch (e: Throwable) {
            null
        }
    }

}