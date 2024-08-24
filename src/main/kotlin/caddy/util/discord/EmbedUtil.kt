package caddy.util.discord

import caddy.Caddy.BuildConfig
import caddy.command.Command
import caddy.util.constants.Colors
import caddy.util.constants.Emojis
import dev.kord.core.behavior.reply
import dev.kord.core.entity.Message
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.embed

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