package caddy.command.utility

import caddy.command.CommandCategory
import caddy.command.createCommand
import caddy.util.*
import com.xenomachina.argparser.default
import dev.kord.common.DiscordTimestampStyle
import dev.kord.common.toMessageFormat
import dev.kord.core.behavior.reply
import dev.kord.core.entity.effectiveName
import dev.kord.rest.Image
import dev.kord.rest.builder.message.actionRow
import dev.kord.rest.builder.message.embed

val UserInfo = createCommand(
    name = "userinfo",
    description = "Display some information about a user",
    usage = buildString {
        appendLine(":userinfo [user]")
        appendLine(" options:")
        appendLine("   -a, --all  Show all permissions")
    },
    category = CommandCategory.UTILITY,
    aliases = listOf("ui", "user")
) { event ->

    val showAllPermissions by flagging("-a", "--all", help = "").default(false)
    val userId by userMentionPositional().default(null)

    val user = if (userId != null) event.kord.getUser(userId!!) else event.message.author
    val member = if (userId != null) event.message.getGuildOrNull()?.getMemberOrNull(userId!!) else event.member

    if (user == null) {
        event.message.replyEmbed {
            color = Colors.Red
            title = "${Emojis.ERROR} Error fetching user details"
            description = "The provided user is invalid or does not exist"
        }
        return@createCommand
    }

    val roles = member?.roleBehaviors?.map { it.asRole() }?.sortedByDescending { it.rawPosition }

    event.message.reply {
        embed {
            color = roles?.firstOrNull { it.color.rgb != 0 }?.color ?: Colors.Blue
            title = buildString {
                append(member?.effectiveName ?: user.effectiveName)
                append(" (${user.tag})")
            }

            description = """
                ${user.mention} ${user.getTagEmotePair()}
                
                ${user.getBadgeEmotes().joinToString("  ")}
            """.trimIndent()

            thumbnail {
                url = (member?.memberAvatar ?: user.avatar)?.cdnUrl?.toUrl() ?: user.defaultAvatar.cdnUrl.toUrl { format = Image.Format.PNG }
            }

            field {
                val timestamp = user.id.timestamp

                name = "Created at"
                value = "${timestamp.toMessageFormat(DiscordTimestampStyle.ShortDate)} (${timestamp.toMessageFormat(DiscordTimestampStyle.RelativeTime)})"
                inline = true
            }

            member?.joinedAt?.let { joined ->
                field {
                    name = "Joined ${event.getGuildOrNull()?.name} at"
                    value = "${joined.toMessageFormat(DiscordTimestampStyle.ShortDate)} (${joined.toMessageFormat(DiscordTimestampStyle.RelativeTime)})"
                    inline = true
                }
            }

            roles?.let { roles ->
                if (roles.isNotEmpty()) field {
                    name = "Roles (${roles.size})"
                    value = roles.joinToString { it.mention }
                }
            }

            member?.getPermissions()?.let { perms ->
                val visiblePerms = if (showAllPermissions) perms.values else perms.values.filter { it in KeyPermissions }
                if (visiblePerms.isNotEmpty()) field {
                    name = "Permissions"
                    value = visiblePerms.joinToString { it.getName() }
                }
            }

            footer {
                text = "ID: ${user.id}"
            }
        }

        user.avatar?.cdnUrl?.toUrl()?.let { url ->
            actionRow {
                linkButton(url) {
                    label = "Avatar"
                }
            }
        }
    }
}