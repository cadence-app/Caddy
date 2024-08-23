package caddy.logging.loggers

import caddy.logging.EventLogger
import caddy.logging.EventType
import caddy.util.Emojis
import dev.kord.core.Kord
import dev.kord.core.event.guild.MemberUpdateEvent
import dev.kord.core.on
import dev.kord.rest.Image

object EditNicknameLogger: EventLogger(
    icon = Emojis.MEMBER_UPDATE,
    title = "Nickname Updated",
    type = EventType.NEUTRAL
) {

    override suspend fun startLogging(kord: Kord) {
        kord.on<MemberUpdateEvent> {
            if (old == null) return@on
            if (member.nickname != old!!.nickname) {
                kord.log {
                    field {
                        name = "Old"
                        value = old?.nickname ?: "*None*"
                    }

                    field {
                        name = "New"
                        value = member.nickname ?: "*None*"
                    }

                    footer {
                        icon = member.avatar?.cdnUrl?.toUrl() ?: member.defaultAvatar.cdnUrl.toUrl { format = Image.Format.PNG }
                        text = "${member.tag} (${member.id})"
                    }
                }
            }
        }
    }

}