package caddy.command.moderation

import caddy.command.CommandCategory
import caddy.command.createCommand
import caddy.util.*
import com.xenomachina.argparser.default
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Permissions
import kotlinx.coroutines.flow.toCollection

val RoleAdd = createCommand(
    name = "roleadd",
    description = "Gives a role to a given member",
    category = CommandCategory.MODERATION,
    aliases = listOf("ra", "add", "+"),
    requiredPermissions = listOf(Permission.ManageRoles)
) { event ->

    val roleResolvable by positional("")

    val targetId by userMentionPositional().default(null)

    val logger = Logger(":roleadd")

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
            title = "${Emojis.ERROR} Error adding role"
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
            title = "${Emojis.ERROR} Error adding role"
            description = "You are not able to add that role"
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
            title = "${Emojis.ERROR} Error adding role"
            description = "Provided target was invalid or does not exist, make sure to provide a valid member in the current server"
        }
        return@createCommand
    }

    try {
        target.addRole(role.id, "Action performed by ${event.message.author?.tag}")
        logger.debug("Role \"${role.name}\" (${role.id}) was added to ${target.tag} (${target.id}) by ${event.message.author?.tag} (${event.message.author?.id})")
        event.message.replyEmbed {
            color = Colors.Blue
            title = "Role added successfully"
            description = "${role.mention} has been added to ${target.mention}"
        }
    } catch (e: Throwable) {
        logger.error("Failed to add role", e)
        event.message.replyEmbed {
            color = Colors.Red
            title = "${Emojis.ERROR} Error adding role"
            description = "An unknown error occurred adding that role"
        }
    }
}