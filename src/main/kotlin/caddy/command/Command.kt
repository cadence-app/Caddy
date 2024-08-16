package caddy.command

import caddy.util.Logger
import caddy.util.createErrorEmbed
import com.xenomachina.argparser.ArgParser
import dev.kord.core.behavior.reply
import dev.kord.core.event.message.MessageCreateEvent

interface Command {

    val name: String
    val description: String
    val category: CommandCategory
    val aliases: List<String>

    suspend fun invoke(argParser: ArgParser, messageCreate: MessageCreateEvent)

}

fun createCommand(
    name: String,
    description: String,
    category: CommandCategory,
    aliases: List<String> = emptyList(),
    run: suspend ArgParser.(MessageCreateEvent) -> Unit
): Command {
    return object : Command {

        private val logger = Logger(":$name")

        override val name: String = name
        override val description: String = description
        override val category: CommandCategory = category
        override val aliases: List<String> = aliases

        override suspend fun invoke(argParser: ArgParser, messageCreate: MessageCreateEvent) {
            try {
                run(argParser, messageCreate)
            } catch (e: Throwable) {
                logger.error("An error occurred running \"$name\"", e)

                runCatching {
                    messageCreate.message.reply {
                        embeds = mutableListOf(createErrorEmbed(e))
                    }
                }
            }
        }

    }
}