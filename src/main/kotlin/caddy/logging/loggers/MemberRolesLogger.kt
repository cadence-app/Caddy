package caddy.logging.loggers

import caddy.logging.EventLogger
import caddy.logging.EventType
import caddy.util.constants.Emojis
import dev.kord.core.Kord
import dev.kord.core.event.guild.MemberUpdateEvent
import dev.kord.core.on
import dev.kord.rest.Image

object MemberRolesLogger: EventLogger(
    icon = Emojis.MEMBER_UPDATE,
    title = "Member roles updated",
    type = EventType.NEUTRAL
) {

    override suspend fun startLogging(kord: Kord) {
        kord.on<MemberUpdateEvent> {
            val added = member.roleIds.filterNot { it in (old?.roleIds ?: emptySet()) }
            val removed = (old?.roleIds ?: emptySet()).filterNot { it in member.roleIds }

            kord.log {
                if (added.isNotEmpty()) field {
                    name = "Added"
                    value = added.joinToString { "<@&$it>" }
                }
                if (removed.isNotEmpty()) field {
                    name = "Removed"
                    value = removed.joinToString { "<@&$it>" }
                }

                footer {
                    icon = member.avatar?.cdnUrl?.toUrl() ?: member.defaultAvatar.cdnUrl.toUrl { format = Image.Format.PNG }
                    text = "${member.tag} (${member.id})"
                }
            }
        }
    }

}