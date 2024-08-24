package caddy.logging.loggers

import caddy.logging.EventLogger
import caddy.logging.EventType
import caddy.util.constants.Emojis
import dev.kord.core.Kord
import dev.kord.core.event.guild.MemberLeaveEvent
import dev.kord.core.on

object MemberRemoveLogger: EventLogger(
    icon = Emojis.MEMBER_REMOVE,
    title = "User Left",
    type = EventType.NEGATIVE
) {

    override suspend fun startLogging(kord: Kord) {
        kord.on<MemberLeaveEvent> {
            kord.log {
                field {
                    name = "User"
                    value = "@${user.tag} (${user.mention})"
                }

                old?.let {
                    it.nickname?.let {
                        field {
                            name = "Nickname"
                            value = it
                        }
                    }

                    if (it.roleIds.isNotEmpty()) {
                        field {
                            name = "Roles"
                            value = it.roleIds.joinToString { rId -> "<@&${rId}>" }
                        }
                    }
                }
            }
        }
    }

}