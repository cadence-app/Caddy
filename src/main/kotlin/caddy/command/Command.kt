package caddy.command

import caddy.command.utility.Help
import caddy.util.Logger
import caddy.util.createErrorEmbed
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.MissingValueException
import com.xenomachina.argparser.ShowHelpException
import com.xenomachina.argparser.UnrecognizedOptionException
import dev.kord.common.Color
import dev.kord.core.behavior.reply
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.rest.builder.message.embed

interface Command {

    val name: String
    val description: String
    val usage: String
    val category: CommandCategory
    val aliases: List<String>

    suspend fun invoke(argParser: ArgParser, messageCreate: MessageCreateEvent)

}

fun createCommand(
    name: String,
    description: String,
    usage: String = ":$name",
    category: CommandCategory,
    aliases: List<String> = emptyList(),
    run: suspend ArgParser.(MessageCreateEvent) -> Unit
): Command {
    return object : Command {

        override val name: String = name
        override val description: String = description
        override val usage: String = usage
        override val category: CommandCategory = category
        override val aliases: List<String> = aliases

        override suspend fun invoke(argParser: ArgParser, messageCreate: MessageCreateEvent) {
            run(argParser, messageCreate)
        }

    }
}