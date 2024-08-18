package caddy.command.utility

import caddy.Caddy.BuildConfig
import caddy.command.CommandCategory
import caddy.command.createCommand
import caddy.onlineTimestamp
import caddy.util.Colors
import caddy.util.Emojis
import caddy.util.replyEmbed
import dev.kord.common.DiscordTimestampStyle
import dev.kord.common.toMessageFormat

private val repoUrlRegex = "(https?|ssh)?(://)?([A-z_\\-0-9]+@)?(github|gitlab|codeberg|gitdab|forgejo)\\.(org|com|net|io)/([A-z_\\-0-9]+/[A-z_\\-0-9]+)(\\.git)?".toRegex()

val BotInfo = createCommand(
    name = "botinfo",
    description = "Displays some information about the bot",
    category = CommandCategory.UTILITY,
    aliases = listOf("bi", "i", "info")
) { event ->
    val appInfo = event.kord.getApplicationInfo()
    val self = event.kord.getSelf()

    event.message.replyEmbed {
        color = Colors.Blue
        title = self.tag
        description = """
            ${Emojis.GITHUB} [${BuildConfig.GIT_REPO_URL.replace(repoUrlRegex, "$6")}](${BuildConfig.GIT_REPO_URL})
        """.trimIndent()

        thumbnail {
            url = self.avatar!!.cdnUrl.toUrl()
        }

        field {
            name = "Creator"
            value = "<@${appInfo.team?.ownerUserId ?: appInfo.ownerId}>"
            inline = true
        }

        field {
            name = "Created at"
            value = appInfo.id.timestamp.toMessageFormat(DiscordTimestampStyle.LongDate)
            inline = true
        }

        field {
            name = "Online since"
            value = "<t:${onlineTimestamp / 1000}:R>"
            inline = true
        }

        field {
            name = "Kord"
            value = "v0.14.0 - [Repo](https://github.com/kordlib/kord)"
            inline = true
        }

        footer {
            text = buildString {
                if (BuildConfig.GIT_LOCAL_CHANGES || BuildConfig.GIT_LOCAL_COMMITS) {
                    append("Local ")
                } else {
                    append("Public ")
                }
                append("Build ${BuildConfig.GIT_COMMIT} on ${BuildConfig.GIT_BRANCH}")
            }
        }
    }
}