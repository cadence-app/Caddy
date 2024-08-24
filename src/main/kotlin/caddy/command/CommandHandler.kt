package caddy.command

import caddy.command.`fun`.Alien
import caddy.command.`fun`.Pronouns
import caddy.command.moderation.Case
import caddy.command.moderation.RoleAdd
import caddy.command.moderation.RoleRemove
import caddy.command.moderation.Warn
import caddy.command.utility.BotInfo
import caddy.command.utility.Help
import caddy.command.utility.Ping
import caddy.command.utility.UserInfo
import caddy.ownerId
import caddy.util.*
import com.xenomachina.argparser.*
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Permissions
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

        // Moderation
        RoleAdd,
        RoleRemove,
        Warn,
        Case,

        // Fun
        Alien,
        Pronouns
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

                if (command.ownerOnly && message.author!!.id != ownerId) {
                    message.replyEmbed {
                        color = Colors.Red
                        title = "${Emojis.ERROR} Error running ${command.name}"
                        description = "This command can only be used by the bot's owner"
                    }
                    return@on
                }

                if (command.requiredPermissions.isNotEmpty()) {
                    if (guildId == null) {
                        message.replyEmbed {
                            color = Colors.Red
                            title = "${Emojis.ERROR} Error running ${command.name}"
                            description = "This command must be ran in a server"
                        }
                        return@on
                    }

                    val roles = message.getGuildOrNull()?.getMemberOrNull(message.data.author.id)?.roleIds?.map { it.value.toString() } ?: emptyList()
                    val effectivePerms = EffectivePermissions.filterKeys { it in roles }.values.flatten()

                    if (roles.isEmpty() || !effectivePerms.containsAll(command.requiredPermissions)) {
                        message.replyEmbed {
                            color = Colors.Red
                            title = "${Emojis.ERROR} Error running ${command.name}"
                            description = "No permissions found"
                        }
                        return@on
                    }

                    // We can just assume that the bot has perms bc we control its permissions
                    // SELFHOSTERS: Give the bot admin or something
                }

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