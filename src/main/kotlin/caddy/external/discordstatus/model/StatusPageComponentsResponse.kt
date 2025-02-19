package caddy.external.discordstatus.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class StatusPageComponentsResponse(
    val page: StatusPage,
    val components: List<StatusPageComponent>
)

@Serializable
data class StatusPage(
    val id: String,
    val name: String,
    val url: String,
    val timeZone: String,
    val updatedAt: Instant
)