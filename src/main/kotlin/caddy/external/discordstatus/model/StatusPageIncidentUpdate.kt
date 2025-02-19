package caddy.external.discordstatus.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class StatusPageIncidentUpdate(
    val id: String,
    val status: StatusPageIncidentStatus, //TODO
    val body: String,
    val incidentId: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val displayAt: Instant,
    val affectedComponents: List<AffectedComponent>?,
    val deliverNotifications: Boolean
)

@Serializable
data class AffectedComponent(
    val code: String,
    val name: String,
    val oldStatus: StatusPageComponentStatus,
    val newStatus: StatusPageComponentStatus
)