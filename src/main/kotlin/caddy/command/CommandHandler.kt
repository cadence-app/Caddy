package caddy.command

import caddy.command.`fun`.Alien
import caddy.command.utility.Ping
import caddy.util.Logger
import com.xenomachina.argparser.ArgParser
import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on

object CommandHandler {

    private val commands = listOf(
        Ping,
        Alien
    )

    private val logger = Logger("CommandHandler")

    private const val PREFIX = ":" // TODO: Make configurable

    private fun resolveCommand(name: String): Command? {
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
            val command = split.removeFirst().replaceFirst(PREFIX, "").lowercase()

            resolveCommand(command)?.let {
                logger.info("Command ${it.name} invoked by ${message.author?.tag ?: "Unknown"}")
                it.invoke(
                    argParser = ArgParser(split.toTypedArray()),
                    this
                )
            }
        }
    }

}