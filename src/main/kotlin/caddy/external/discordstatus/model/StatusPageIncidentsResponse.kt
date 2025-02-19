package caddy.external.discordstatus.model

import kotlinx.serialization.Serializable

@Serializable
data class StatusPageIncidentsResponse(
    val page: StatusPage,
    val incidents: List<StatusPageIncident>
)