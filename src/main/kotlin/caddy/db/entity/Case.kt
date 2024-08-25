package caddy.db.entity

import caddy.util.constants.Colors
import caddy.util.constants.Emojis
import dev.kord.common.Color
import kotlinx.datetime.Instant
import org.ktorm.entity.Entity

interface Case: Entity<Case> {
    var id: Int
    var type: CaseType
    var actorId: String
    var targetId: String
    var reason: String?
    var createdAt: Instant
    var logMessageId: String?

    companion object : Entity.Factory<Case>()
}

enum class CaseType(
    val icon: String,
    val color: Color,
    val label: String
) {
    WARN(Emojis.WARN, Colors.Yellow, "Warning"),
    MUTE(Emojis.MEMBER_UPDATE, Colors.Red, "Timeout"),
    KICK(Emojis.MEMBER_UPDATE, Colors.Red, "Kick"),
    BAN(Emojis.MEMBER_UPDATE, Colors.Red, "Ban")
}