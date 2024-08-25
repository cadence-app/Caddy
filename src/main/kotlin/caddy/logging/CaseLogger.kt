package caddy.logging

import caddy.db.CaddyDB
import caddy.db.cases
import caddy.db.entity.Case
import caddy.util.constants.Constants
import caddy.util.discord.createCaseEmbed
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.behavior.edit
import dev.kord.core.entity.User
import dev.kord.core.entity.channel.TextChannel
import org.ktorm.entity.update

object CaseLogger {

    suspend fun logCase(kord: Kord, actor: User, case: Case) {
        (kord.getChannel(Snowflake(Constants.MOD_LOGS_CHANNEL))?.asChannel() as TextChannel).createMessage {
            embeds = mutableListOf(createCaseEmbed(case, actor))
        }.also {
            CaddyDB.cases.update(case.apply { logMessageId = it.id.toString() })
        }
    }

    suspend fun updateLog(kord: Kord, actor: User?, case: Case) {
        case.logMessageId?.let {
            (kord.getChannel(Snowflake(Constants.MOD_LOGS_CHANNEL))?.asChannel() as TextChannel).getMessageOrNull(Snowflake(it))?.edit {
                embeds = mutableListOf(createCaseEmbed(case, actor))
            }
        }
    }

}