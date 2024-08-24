package caddy.logging.loggers

import caddy.logging.EventLogger
import caddy.logging.EventType
import caddy.util.constants.Emojis
import dev.kord.core.Kord
import dev.kord.core.event.guild.MemberJoinEvent
import dev.kord.core.on

object MemberAddLogger: EventLogger(
    icon = Emojis.MEMBER_ADD,
    title = "New Member!",
    type = EventType.NEUTRAL
) {

    override suspend fun startLogging(kord: Kord) {
        kord.on<MemberJoinEvent> {
            kord.log {
                field {
                    name = "Member"
                    value = "@${member.tag} (${member.mention})"
                }
            }
        }
    }

}