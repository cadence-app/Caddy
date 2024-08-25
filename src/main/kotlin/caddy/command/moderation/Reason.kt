package caddy.command.moderation

import caddy.command.CommandCategory
import caddy.command.createCommand
import caddy.db.CaddyDB
import caddy.db.cases
import caddy.logging.CaseLogger
import caddy.util.constants.Colors
import caddy.util.constants.Constants
import caddy.util.constants.Emojis
import caddy.util.discord.replyEmbed
import dev.kord.common.entity.Snowflake
import org.ktorm.dsl.eq
import org.ktorm.entity.find
import org.ktorm.entity.update

val Reason = createCommand(
    name = "reason",
    description = "Update or set the reason for a particular case",
    usage = ":reason <case #> <reason>",
    category = CommandCategory.MODERATION,
    allowedRoles = Constants.ModRoles
) { event ->

    val caseId by positional("") { toIntOrNull() }

    val reason by positionalList("")

    val reasonStr = reason.joinToString(" ")

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
            title = "${Emojis.ERROR} Error updating reason"
            description = "Case #${caseId} does not exist"
        }
        return@createCommand
    }

    CaddyDB.cases.update(case.apply { this.reason = reasonStr })

    event.message.replyEmbed {
        color = Colors.Blue
        title = "${Emojis.CHECK} Successfully updated case #${case.id}"
        description = "*Use `:case ${case.id}` to view all details*"

        field {
            name = "Reason"
            value = reasonStr
        }
    }

    val mod = event.kord.getUser(Snowflake(case.actorId))

    CaseLogger.updateLog(event.kord, mod, case)
}