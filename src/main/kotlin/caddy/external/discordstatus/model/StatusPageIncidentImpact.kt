package caddy.external.discordstatus.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class StatusPageIncidentImpact(
    val emoji: String
) {
    @SerialName("none") None("🟢"),
    @SerialName("maintenance") Maintenance("🚧"),
    @SerialName("minor") Minor("🟡"),
    @SerialName("major") Major("🟠"),
    @SerialName("critical") Critical("🔴")
}