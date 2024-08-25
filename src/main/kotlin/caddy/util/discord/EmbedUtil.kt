package caddy.util.discord

import caddy.Caddy.BuildConfig
import caddy.command.Command
import caddy.db.entity.Case
import caddy.util.constants.Colors
import caddy.util.constants.Emojis
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.reply
import dev.kord.core.entity.Message
import dev.kord.core.entity.User
import dev.kord.rest.Image
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.embed
import dev.kord.rest.route.DiscordCdn

fun Command.createErrorEmbed(throwable: Throwable): EmbedBuilder {
    return EmbedBuilder().apply {
        color = Colors.Red
        title = "${Emojis.ERROR} Command failed"

        this.description = """
                           An unknown error occurred trying to run "$name"
            
                           If this keeps happening don't be afraid to [make an issue](${BuildConfig.GIT_REPO_URL}/issues/new)
                           """.trimIndent()

        field {
            this.name = "Error"
            value = """```java
                    ${throwable::class.qualifiedName}: ${throwable.message}
                    ```""".trimIndent()
        }
    }
}

suspend inline fun Message.replyEmbed(builder: EmbedBuilder.() -> Unit): Message {
    return reply {
        embed(builder)
    }
}

fun createCaseEmbed(case: Case, mod: User?) = EmbedBuilder().apply {
    color = case.type.color

    author {
        name = "Case #${case.id} | ${case.type.label}"
        icon = Emojis.getUrl(case.type.icon)
    }

    field {
        name = "User"
        value = "<@${case.targetId}>"
    }

    field {
        name = "Reason"
        value = case.reason ?: "*No reason provided, use `:reason ${case.id} <reason>` to set one*"
    }

    footer {
        icon = mod?.avatar?.cdnUrl?.toUrl() ?:
                mod?.defaultAvatar?.cdnUrl?.toUrl { format = Image.Format.PNG } ?:
                DiscordCdn.defaultUserAvatar(Snowflake(case.actorId)).toUrl { format = Image.Format.PNG }
        text = "Mod: @${mod?.tag ?: "Unknown"} (${case.actorId})"
    }

    timestamp = case.createdAt
}