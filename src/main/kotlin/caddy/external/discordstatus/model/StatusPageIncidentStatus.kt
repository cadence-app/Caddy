package caddy.external.discordstatus.model

import caddy.util.constants.Emojis
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class StatusPageIncidentStatus(
    val emoji: String
) {
    @SerialName("investigating") Investigating("🕵️‍♂️"),
    @SerialName("identified") Identified("💡"),
    @SerialName("monitoring") Monitoring("📊"),
    @SerialName("resolved") Resolved(Emojis.CHECK),
    @SerialName("scheduled") Scheduled("📅"),
    @SerialName("in_progress") `In Progress`("⏳"),
    @SerialName("verifying") Verifying("🧪"),
    @SerialName("completed") Completed(Emojis.CHECK)
}