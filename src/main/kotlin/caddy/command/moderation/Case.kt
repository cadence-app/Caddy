package caddy.command.moderation

import caddy.command.CommandCategory
import caddy.command.createCommand
import caddy.db.CaddyDB
import caddy.db.cases
import caddy.util.constants.Colors
import caddy.util.constants.Emojis
import caddy.util.discord.replyEmbed
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Snowflake
import dev.kord.rest.Image
import dev.kord.rest.route.DiscordCdn
import org.ktorm.dsl.eq
import org.ktorm.entity.find

val Case = createCommand(
    name = "case",
    description = "Retrieve details about a case",
    usage = ":case <#>",
    category = CommandCategory.MODERATION,
    requiredPermissions = listOf(Permission.ModerateMembers)
) { event ->

    val caseId by positional("") { toIntOrNull() }

    if (caseId == null) {
        event.message.replyEmbed {
            color = Colors.Red
            title = "${Emojis.ERROR} Error fetching case"
            description = "Provided case number is invalid"
        }
        return@createCommand
    }

    val case = CaddyDB.cases.find { it.id eq caseId!! }

    if (case == null) {
        event.message.replyEmbed {
            color = Colors.Red
            title = "${Emojis.ERROR} Error fetching case"
            description = "No case with that number exists"
        }
        return@createCommand
    }

    val mod = event.kord.getUser(Snowflake(case.actorId))

    event.message.replyEmbed {
        color = case.type.color

        author {
            name = "Case #${case.id} | ${case.type.label}"
            icon = Emojis.getUrl(case.type.icon)
        }

        field {
            name = "User"
            value = "<@${case.targetId}>"
        }

        field {
            name = "Reason"
            value = case.reason ?: "*No reason provided, use `:reason` to set one*"
        }

        footer {
            icon = mod?.avatar?.cdnUrl?.toUrl() ?:
                   mod?.defaultAvatar?.cdnUrl?.toUrl { format = Image.Format.PNG } ?:
                   DiscordCdn.defaultUserAvatar(Snowflake(case.actorId)).toUrl { format = Image.Format.PNG }
            text = "Mod: @${mod?.tag ?: "Unknown"} (${case.actorId})"
        }

        timestamp = case.createdAt
    }
}