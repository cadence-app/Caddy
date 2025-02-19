package caddy.external.discordstatus.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class StatusPageComponentStatus(
    val emoji: String
) {
    @SerialName("operational") Operational("\uD83D\uDFE2"),
    @SerialName("degraded_performance") Degraded("\uD83D\uDFE1"),
    @SerialName("partial_outage") `Partial Outage`("\uD83D\uDFE0"),
    @SerialName("major_outage") `Major Outage`("\uD83D\uDD34"),
    @SerialName("under_maintenance") Maintenance("🚧")
}