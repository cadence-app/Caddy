package caddy.command.utility

import caddy.command.CommandCategory
import caddy.command.createCommand
import caddy.util.Colors
import dev.kord.core.behavior.edit
import dev.kord.core.behavior.reply
import dev.kord.rest.builder.message.embed

val Ping = createCommand(
    name = "ping",
    description = "Pings the bot",
    category = CommandCategory.UTILITY
) { event ->
    val pingStart = System.currentTimeMillis()

    event.message
        .reply {
            embed {
                color = Colors.Blue
                title = "Pinging..."
            }
        }
        .edit {
            embed {
                color = Colors.Blue
                title = ":ping_pong:  Pong!"
                description = "ℹ This only measures the api latency for the bot, not for the user"

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