package caddy.logging

import caddy.logging.loggers.EditNicknameLogger
import caddy.logging.loggers.MemberAddLogger
import caddy.logging.loggers.MemberRemoveLogger
import caddy.logging.loggers.MemberRolesLogger
import caddy.util.constants.Colors
import caddy.util.constants.Constants
import caddy.util.constants.Emojis
import dev.kord.common.Color
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.entity.channel.TextChannel
import dev.kord.rest.builder.message.EmbedBuilder
import kotlinx.datetime.Clock

open class EventLogger(
    private val channelId: String = Constants.ACTION_LOGS_CHANNEL,
    private val icon: String,
    private val title: String,
    private val type: EventType
) {

    open suspend fun startLogging(kord: Kord) {}

    suspend fun Kord.log(fields: EmbedBuilder.() -> Unit) {
        (getChannel(Snowflake(channelId))?.asChannel() as TextChannel).createMessage {
            embeds = mutableListOf(renderEmbed(fields))
        }
    }

    private fun renderEmbed(fields: EmbedBuilder.() -> Unit): EmbedBuilder {
        return EmbedBuilder().apply {
            color = type.color
            author {
                icon = Emojis.getUrl(this@EventLogger.icon)
                name = this@EventLogger.title
            }

            timestamp = Clock.System.now()

            fields()
        }
    }

}

enum class EventType(val color: Color) {
    NEUTRAL(Colors.Blue),
    WARN(Colors.Yellow),
    NEGATIVE(Colors.Red)
}

suspend fun Kord.startAllEventLoggers() {
    listOf(
        EditNicknameLogger,
        MemberAddLogger,
        MemberRemoveLogger,
        MemberRolesLogger
    ).onEach {
        it.startLogging(this)
    }
}