package caddy.command.utility

import caddy.command.CommandCategory
import caddy.command.CommandHandler
import caddy.command.createCommand
import caddy.paging.createPaginator
import caddy.util.Colors
import caddy.util.Emojis
import caddy.util.replyEmbed
import com.xenomachina.argparser.default

val Help = createCommand(
    name = "help",
    description = "View information about 1 or more commands",
    usage = buildString {
        appendLine(":help [command]")
        appendLine(" options:")
        appendLine("   --category, -c  View a list of commands in a given category")
    },
    category = CommandCategory.UTILITY,
    aliases = listOf("h", "?")
) { event ->

    val category by storing("--category", "-c", help = "") {
        CommandCategory.entries.find { it.name == this.uppercase() }
    }.default(null)

    val command by positional("").default(null)

    when {
        command != null -> {
            event.message.replyEmbed {
                val cmd = CommandHandler.resolveCommand(command!!.lowercase())
                if (cmd != null) {
                    color = Colors.Blue
                    title = cmd.name
                    description = cmd.description

                    if (cmd.aliases.isNotEmpty()) field {
                        name = "Aliases"
                        value = cmd.aliases.joinToString()
                        inline = true
                    }

                    field {
                        name = "Category"
                        value = cmd.category.title
                        inline = true
                    }

                    field {
                        name = "Usage"
                        value = "```\n${cmd.usage}```"
                    }
                } else {
                    color = Colors.Red
                    title = "${Emojis.ERROR} Error running help"
                    description = "Unable to find a command with the name or alias `$command`"

                    footer {
                        text = "Use :help to get a list of available commands"
                    }
                }
            }
        }

        category != null -> {
            val commandsInCategory = CommandHandler.commands.filter { it.category == category }

            event.message.createPaginator(commandsInCategory) { page ->
                color = Colors.Blue
                title = "${category!!.title} Commands"
                description = category!!.description

                page.forEach { cmd ->
                    field {
                        name = "${cmd.name}${if (cmd.aliases.isNotEmpty()) " (" + cmd.aliases.joinToString() + ")" else ""}"
                        value = cmd.description
                        inline = true
                    }
                }
            }
        }

        else -> {
            event.message.replyEmbed {
                color = Colors.Blue
                title = "Help"
                description = "All of the commands this bot supports\n \nUse `:help [command]` to view information about an individual command"

                CommandCategory.entries.forEach { category ->
                    val cmds = CommandHandler.commands.filter { cmd -> cmd.category == category }
                    if (cmds.isNotEmpty()) {
                        field {
                            name = category.title
                            value = CommandHandler.commands.filter { cmd -> cmd.category == category }.joinToString { it.name }
                        }
                    }
                }
            }
        }
    }
}

