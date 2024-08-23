package caddy.command

import com.xenomachina.argparser.ArgParser
import dev.kord.common.entity.Permission
import dev.kord.core.event.message.MessageCreateEvent

interface Command {

    val name: String
    val description: String
    val usage: String
    val category: CommandCategory
    val aliases: List<String>
    val requiredPermissions: List<Permission>

    suspend fun invoke(argParser: ArgParser, messageCreate: MessageCreateEvent)

}

fun createCommand(
    name: String,
    description: String,
    usage: String = ":$name",
    category: CommandCategory,
    aliases: List<String> = emptyList(),
    requiredPermissions: List<Permission> = emptyList(),
    run: suspend ArgParser.(MessageCreateEvent) -> Unit
): Command {
    return object : Command {

        override val name: String = name
        override val description: String = description
        override val usage: String = usage
        override val category: CommandCategory = category
        override val aliases: List<String> = aliases
        override val requiredPermissions: List<Permission> = requiredPermissions

        override suspend fun invoke(argParser: ArgParser, messageCreate: MessageCreateEvent) {
            run(argParser, messageCreate)
        }

    }
}