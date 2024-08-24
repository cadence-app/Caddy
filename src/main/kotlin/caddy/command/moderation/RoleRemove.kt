package caddy.command.moderation

import caddy.command.CommandCategory
import caddy.command.createCommand
import caddy.util.*
import com.xenomachina.argparser.default
import dev.kord.common.entity.Permission

val RoleRemove = createCommand(
    name = "roleremove",
    description = "Removes a role from a given member",
    usage = buildString {
        appendLine(":roleremove <role> <member>")
        appendLine("Note: You can also use this command by replying")
    },
    category = CommandCategory.MODERATION,
    aliases = listOf("rr", "remove", "-"),
    requiredPermissions = listOf(Permission.ManageRoles)
) { event ->

    val roleResolvable by positional("")

    val targetId by userMentionPositional().default(null)

    val logger = Logger(":roleremove")

    val role = event
        .message
        .getGuild()
        .roleBehaviors
        .mapNotNull { it.asRoleOrNull() }
        .sortedByDescending { it.rawPosition }
        .firstOrNull {
            it.id.toString() == roleResolvable.replaceFirst(roleMentionRegex, "$2")
                    || it.name.lowercase().contains(roleResolvable.lowercase())
        }

    if (role == null) {
        event.message.replyEmbed {
            color = Colors.Red
            title = "${Emojis.ERROR} Error removing role"
            description = "Desired role does not exist"
        }
        return@createCommand
    }

    logger.debug("Resolved role \"${role.name}\" (${role.id}) from \"${roleResolvable}\"")

    val highestRole = event
        .member!!
        .roleBehaviors
        .map { it.asRole() }
        .maxByOrNull { it.rawPosition }

    if (
        highestRole == null ||
        highestRole.rawPosition <= role.rawPosition
    ) {
        event.message.replyEmbed {
            color = Colors.Red
            title = "${Emojis.ERROR} Error removing role"
            description = "You are not able to remove that role"
        }
        return@createCommand
    }

    val target = if (targetId != null)
        event
            .message
            .getGuild()
            .getMemberOrNull(targetId!!)
    else
        event
            .message
            .messageReference
            ?.message
            ?.asMessage()
            ?.author
            ?.asMemberOrNull(event.guildId!!)

    if (target == null) {
        event.message.replyEmbed {
            color = Colors.Red
            title = "${Emojis.ERROR} Error removing role"
            description = "Provided target was invalid or does not exist, make sure to provide a valid member in the current server"
        }
        return@createCommand
    }

    try {
        target.removeRole(role.id, "Action performed by ${event.message.author?.tag}")
        logger.debug("Role \"${role.name}\" (${role.id}) was removed from ${target.tag} (${target.id}) by ${event.message.author?.tag} (${event.message.author?.id})")
        event.message.replyEmbed {
            color = Colors.Blue
            title = "Role removed successfully"
            description = "${Emojis.CHECK} ${role.mention} has been removed from ${target.mention}"
        }
    } catch (e: Throwable) {
        logger.error("Failed to remove role", e)
        event.message.replyEmbed {
            color = Colors.Red
            title = "${Emojis.ERROR} Error removing role"
            description = "An unknown error occurred removing that role"
        }
    }
}