package caddy.command.utility

import caddy.command.CommandCategory
import caddy.command.createCommand
import caddy.external.discordstatus.DiscordStatusClient
import caddy.external.discordstatus.model.StatusPageIncidentStatus
import caddy.util.constants.Colors
import caddy.util.constants.Emojis
import caddy.util.discord.replyEmbed
import dev.kord.common.toMessageFormat
import dev.kord.core.behavior.reply
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.allowedMentions

val desiredComponents = listOf("API", "Gateway", "Android", "Desktop", "Push Notifications", "Voice")

val DiscordStatus = createCommand(
    name = "discord-status",
    description = "Reports Discord's API status",
    category = CommandCategory.UTILITY,
    aliases = listOf("status", "dstatus", "ds")
) { event ->
    val components = DiscordStatusClient.tryGetComponents()
    val incidents = DiscordStatusClient.tryGetIncidents()

    if (components == null || incidents == null) {
        event.message.replyEmbed {
            color = Colors.Red
            title = "${Emojis.ERROR} Error getting Discord's status"
        }
        return@createCommand
    }

    val embeds = mutableListOf<EmbedBuilder>()
    val desired = components.components.filter { it.name in desiredComponents }

    embeds.add(EmbedBuilder().apply {
        color = Colors.Blue
        title = "Discord API Status"
        timestamp = desired.maxByOrNull { it.updatedAt }!!.updatedAt

        desired.forEach {
            field {
                name = it.name
                value = "${it.status.emoji} ${it.status.name}"
                inline = true
            }
        }

        footer {
            text = "Last Updated"
        }
    })

    incidents.incidents.filter { it.status != StatusPageIncidentStatus.Resolved }.forEach { incident ->
        embeds.add(EmbedBuilder().apply {
            color = Colors.Red
            title = incident.name
            description = "**Status**: ${incident.status.emoji} ${incident.status.name}\n**Impact**: ${incident.impact.emoji} ${incident.impact.name}"
            url = "https://discordstatus.com/incidents/${incident.id}"

            incident.incidentUpdates.forEach { update ->
                field {
                    name = update.status.name
                    value = update.body + "\n${update.createdAt.toMessageFormat()}"
                }
            }
        })
    }

    event.message.reply {
        allowedMentions { repliedUser = false }
        this.embeds = embeds
    }
}