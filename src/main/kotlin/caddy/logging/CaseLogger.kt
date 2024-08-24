package caddy.logging

import caddy.db.entity.Case
import caddy.util.Emojis
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.entity.User
import dev.kord.core.entity.channel.TextChannel
import dev.kord.rest.Image
import dev.kord.rest.builder.message.embed

const val MOD_LOG_CHANNEL_ID = "1276418406500925526"

object CaseLogger {

    suspend fun logCase(kord: Kord, actor: User, case: Case) {
        (kord.getChannel(Snowflake(MOD_LOG_CHANNEL_ID))?.asChannel() as TextChannel).createMessage {
            embed {
                color = case.type.color

                author {
                    name = "Case #${case.id} | ${case.type.label}"
                    icon = Emojis.getUrl(case.type.icon)
                }

                field {
                    name = "User"
                    value = "<@${case.targetId}> (${case.id})"
                }

                field {
                    name = "Reason"
                    value = case.reason ?: "*No reason provided, use `:reason` to set one*"
                }

                footer {
                    icon = actor.avatar?.cdnUrl?.toUrl() ?: actor.defaultAvatar.cdnUrl.toUrl { format = Image.Format.PNG }
                    text = "Mod: @${actor.tag} (${case.actorId})"
                }

                timestamp = case.createdAt
            }
        }
    }

}