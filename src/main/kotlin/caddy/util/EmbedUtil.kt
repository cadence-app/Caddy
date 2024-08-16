package caddy.util

import caddy.command.Command
import dev.kord.common.Color
import dev.kord.rest.builder.message.EmbedBuilder

fun Command.createErrorEmbed(throwable: Throwable): EmbedBuilder {
    return EmbedBuilder().apply {
        color = Color(0xF38BA8)
        title = "❌ Command failed"

        this.description = """
            An unknown error occurred trying to run "$name"
            
            If this keeps happening don't be afraid to [make an issue](https://github.com/cadence-app/Caddy/issues/new)
        """.trimIndent()

        field {
            this.name = "Error"
            value = """
                ```java
                ${throwable::class.qualifiedName}: ${throwable.message}
                ```
            """.trimIndent().also { println(it.length) }
        }
    }
}