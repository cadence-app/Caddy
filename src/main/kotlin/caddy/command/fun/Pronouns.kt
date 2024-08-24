package caddy.command.`fun`

import caddy.command.CommandCategory
import caddy.command.createCommand
import caddy.util.Colors
import caddy.util.Emojis
import caddy.util.replyEmbed
import io.ktor.client.request.*
import io.ktor.http.*

val Pronouns = createCommand(
    name = "pronouns",
    description = "Sets the bots pronouns in the current guild",
    usage = ":pronouns <clear|pronouns>",
    category = CommandCategory.FUN,
    ownerOnly = true
) { event ->

    val pronouns by positional("")

    if (event.guildId == null) {
        event.message.replyEmbed {
            color = Colors.Red
            title = "${Emojis.ERROR} Error setting pronouns"
            description = "This command must be ran in a server"
        }
        return@createCommand
    }

    if (pronouns.length !in 1..40) {
        event.message.replyEmbed {
            color = Colors.Red
            title = "${Emojis.ERROR} Failed to update pronouns"
            description = "Pronouns must be between 1 and 40 characters long"
        }
        return@createCommand
    }

    event.kord.resources.httpClient.patch("https://discord.com/api/v10/guilds/${event.guildId}/members/@me") {
        setBody(
            """
                {
                    "pronouns": ${if (pronouns.lowercase() == "clear") "null" else "\"$pronouns\""}
                }
            """.trimIndent()
        )
        header(HttpHeaders.ContentType, ContentType.Application.Json)
        header(HttpHeaders.Authorization, "Bot ${event.kord.resources.token}")
    }

    event.message.replyEmbed {
        color = Colors.Blue
        title = "${Emojis.CHECK} Successfully updated pronouns"
        description = if (pronouns.lowercase() == "clear") "Cleared pronouns" else "My new pronouns are `${pronouns}`"
    }
}