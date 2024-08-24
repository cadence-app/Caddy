package caddy.command.moderation

import caddy.command.CommandCategory
import caddy.command.createCommand
import caddy.db.CaddyDB
import caddy.db.cases
import caddy.db.entity.Case
import caddy.db.entity.CaseType
import caddy.logging.CaseLogger
import caddy.util.constants.Colors
import caddy.util.constants.Constants
import caddy.util.constants.Emojis
import caddy.util.discord.replyEmbed
import caddy.util.userMentionPositional
import dev.kord.common.entity.Permission
import dev.kord.rest.Image
import kotlinx.datetime.Clock
import org.ktorm.entity.add

val Warn = createCommand(
    name = "warn",
    description = "Warns a user",
    usage = ":warn <user> <reason>",
    category = CommandCategory.MODERATION,
    aliases = listOf("w"),
    allowedRoles = Constants.ModRoles
) { event ->

    val target by userMentionPositional()

    val reason by positionalList("")

    val reasonStr = reason.joinToString(" ")

    if (target == null) {
        event.message.replyEmbed {
            color = Colors.Red
            title = "${Emojis.ERROR} Error warning user"
            description = "No valid user was provided"
        }
        return@createCommand
    }

    val case = Case {
        type = CaseType.WARN
        actorId = event.message.data.author.id.toString()
        targetId = target.toString()
        this.reason = reasonStr
        createdAt = Clock.System.now()
    }

    CaddyDB.cases.add(case)
    val actor = event.message.author!!

    CaseLogger.logCase(event.kord, actor, case)

    event.message.replyEmbed {
        title = "${Emojis.WARN} Issued warning | Case #${case.id}"
        color = Colors.Yellow

        field {
            name = "To"
            value = "<@${target}>"
        }

        field {
            name = "Reason"
            value = reasonStr
        }

        footer {
            icon = actor.avatar?.cdnUrl?.toUrl() ?: actor.defaultAvatar.cdnUrl.toUrl { format = Image.Format.PNG }
            text = "From: @${actor.tag} (${case.actorId})"
        }
    }
}