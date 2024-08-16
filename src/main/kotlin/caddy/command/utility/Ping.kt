package caddy.command.utility

import caddy.command.CommandCategory
import caddy.command.createCommand
import dev.kord.common.Color
import dev.kord.core.behavior.edit
import dev.kord.core.behavior.reply
import dev.kord.rest.builder.message.embed

val Ping = createCommand(
    name = "ping",
    description = "Pings the bot",
    category = CommandCategory.UTILITY
) { event ->
    val pingStart = System.currentTimeMillis()
    val embedColor = Color(0x9D9DF6)

    event.message
        .reply {
            embed {
                color = embedColor
                title = "Pinging..."
            }
        }
        .edit {
            embed {
                color = embedColor
                title = ":ping_pong:  Pong!"
                description = "ℹ This only measures the api latency for the bot and not for the user"

                field {
                    name = "API ping"
                    value = "${System.currentTimeMillis() - pingStart}ms"
                }

                field {
                    name = "Websocket ping"
                    value = "${event.kord.gateway.averagePing?.inWholeMilliseconds}ms"
                }
            }
        }
}