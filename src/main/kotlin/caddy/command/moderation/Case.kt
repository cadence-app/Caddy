package caddy.command.moderation

import caddy.command.CommandCategory
import caddy.command.createCommand
import caddy.db.CaddyDB
import caddy.db.cases
import caddy.util.constants.Colors
import caddy.util.constants.Constants
import caddy.util.constants.Emojis
import caddy.util.discord.createCaseEmbed
import caddy.util.discord.replyEmbed
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.reply
import dev.kord.rest.Image
import dev.kord.rest.route.DiscordCdn
import org.ktorm.dsl.eq
import org.ktorm.entity.find

val Case = createCommand(
    name = "case",
    description = "Retrieve details about a case",
    usage = ":case <#>",
    category = CommandCategory.MODERATION,
    allowedRoles = Constants.ModRoles
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
            description = "Case #${caseId} does not exist"
        }
        return@createCommand
    }

    val mod = event.kord.getUser(Snowflake(case.actorId))

    event.message.reply {
        embeds = mutableListOf(createCaseEmbed(case, mod))
    }
}