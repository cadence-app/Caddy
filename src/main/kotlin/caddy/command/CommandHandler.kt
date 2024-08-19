package caddy.command

import caddy.command.`fun`.Alien
import caddy.command.utility.BotInfo
import caddy.command.utility.Help
import caddy.command.utility.Ping
import caddy.command.utility.UserInfo
import caddy.util.*
import com.xenomachina.argparser.*
import dev.kord.core.Kord
import dev.kord.core.behavior.reply
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on

object CommandHandler {

    val commands: List<Command> = listOf(
        // Utility
        Ping,
        Help,
        BotInfo,
        UserInfo,

        // Fun
        Alien
    )

    private val logger = Logger("CommandHandler")

    private const val PREFIX = ":" // TODO: Make configurable

    fun resolveCommand(name: String): Command? {
        return commands.find { cmd ->
            cmd.name == name || cmd.aliases.contains(name)
        }
    }

    fun listen(
        kord: Kord
    ) {
        kord.on<MessageCreateEvent> {
            if (message.author?.isBot != false) return@on
            if (message.content.isBlank()) return@on
            if (!message.content.startsWith(PREFIX)) return@on

            logger.debug("${message.author!!.tag}: ${message.content}")

            val split = message.content.split(" ").toMutableList()
            val commandName = split.removeFirst().replaceFirst(PREFIX, "").lowercase()

            resolveCommand(commandName)?.let { command ->
                logger.info("Command \"${command.name}\" invoked by ${message.author?.tag ?: "Unknown"}")

                try {
                    command.invoke(argParser = ArgParser(split.toTypedArray()),this)
                }

                catch (e: ShowHelpException) { Help.invoke(ArgParser(arrayOf(commandName)), this) }

                catch (e: OptionMissingRequiredArgumentException) {
                    message.replyEmbed {
                        color = Colors.Red
                        title = "${Emojis.ERROR} Command failed"

                        this.description = "One or more options are missing a required argument"
                    }
                }

                catch (e: MissingValueException) {
                    message.replyEmbed {
                        color = Colors.Red
                        title = "${Emojis.ERROR} Command failed"

                        this.description = "One or more options are missing"
                    }
                }

                catch (e: Throwable) {
                    logger.error("An error occurred running \"${command.name}\"", e)

                    runCatching {
                        message.reply {
                            embeds = mutableListOf(command.createErrorEmbed(e))
                        }
                    }
                }

            }
        }
    }

}