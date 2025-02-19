package caddy.external.discordstatus.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class StatusPageIncident(
    val id: String,
    val name: String,
    val status: StatusPageIncidentStatus,
    val createdAt: Instant?,
    val updatedAt: Instant?,
    val monitoringAt: Instant?,
    val resolvedAt: Instant?,
    val impact: StatusPageIncidentImpact,
    val shortlink: String,
    val startedAt: Instant,
    val pageId: String,
    val incidentUpdates: List<StatusPageIncidentUpdate> = emptyList()
)
