package caddy.command.moderation

import caddy.command.CommandCategory
import caddy.command.createCommand
import caddy.db.CaddyDB
import caddy.db.cases
import caddy.paging.createPaginator
import caddy.util.constants.Colors
import caddy.util.constants.Emojis
import caddy.util.discord.replyEmbed
import caddy.util.userMentionPositional
import dev.kord.common.DiscordTimestampStyle
import dev.kord.common.toMessageFormat
import dev.kord.rest.Image
import org.ktorm.dsl.eq
import org.ktorm.entity.filter
import org.ktorm.entity.sortedByDescending
import org.ktorm.entity.toList

val Cases = createCommand(
    name = "cases",
    description = "View all cases belonging to a specific user",
    category = CommandCategory.MODERATION
) { event ->

    val targetId by userMentionPositional()

    if (targetId == null) {
        event.message.replyEmbed {
            color = Colors.Red
            title = "${Emojis.ERROR} Error viewing cases"
            description = "Could not resolve a user with the provided target, try using a mention or id"
        }
        return@createCommand
    }

    val user = event.kord.getUser(targetId!!)

    if (user == null) {
        event.message.replyEmbed {
            color = Colors.Red
            title = "${Emojis.ERROR} Error viewing cases"
            description = "User with id `$targetId` does not exist"
        }
        return@createCommand
    }

    val cases = CaddyDB.cases
        .filter { it.targetId eq targetId!!.toString() }
        .sortedByDescending { it.createdAt }
        .toList()

    event.message.createPaginator(cases) { page ->
        color = Colors.Blue

        author {
            name = "${user.tag} (${user.id})"
            icon = user.avatar?.cdnUrl?.toUrl() ?: user.defaultAvatar.cdnUrl.toUrl { format = Image.Format.PNG }
            url = "https://discord.com/users/${user.id}"
        }

        if (page.isEmpty()) {
            description = "*No items to display*"
        }

        page.forEach { case ->
            field {
                name = "Case #${case.id} | ${case.type.label}"
                value = buildString {
                    appendLine(case.reason?.let { "```\n$it\n```" } ?: "*No reason provided, use `:reason ${case.id} <reason>` to set one*")
                    append("**Moderator**: <@${case.actorId}> | ${case.createdAt.toMessageFormat(DiscordTimestampStyle.ShortDateTime)}")
                }
            }
        }
    }
}