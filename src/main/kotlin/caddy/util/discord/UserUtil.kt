package caddy.util.discord

import caddy.util.constants.Emojis
import dev.kord.common.entity.UserFlag
import dev.kord.common.entity.UserFlags
import dev.kord.core.entity.User

fun User.getBadgeEmotes(): List<String> {
    return buildList {
        val flags = publicFlags ?: UserFlags()
        if (UserFlag.DiscordEmployee in flags) add(Emojis.DISCORD_STAFF)
        if (UserFlag.DiscordPartner in flags) add(Emojis.PARTNER)
        if (UserFlag.DiscordCertifiedModerator in flags) add(Emojis.MOD_PROGRAM_ALUMNI)
        if (UserFlag.BugHunterLevel2 in flags) add(Emojis.BUG_HUNTER_2)
        if (UserFlag.BugHunterLevel1 in flags) add(Emojis.BUG_HUNTER_1)
        if (UserFlag.ActiveDeveloper in flags) add(Emojis.ACTIVE_DEV)
        if (UserFlag.VerifiedBotDeveloper in flags) add(Emojis.VERIFIED_DEV)
        if (UserFlag.HypeSquad in flags) add(Emojis.HYPESQUAD_EVENTS)
        if (UserFlag.HouseBalance in flags) add(Emojis.BALANCE)
        if (UserFlag.HouseBravery in flags) add(Emojis.BRAVERY)
        if (UserFlag.HouseBrilliance in flags) add(Emojis.BRILLIANCE)
        if (UserFlag.EarlySupporter in flags) add(Emojis.EARLY_SUPPORTER)
    }
}

fun User.getTagEmotePair(): String {
    return buildString {
        if (isBot) {
            if (publicFlags?.values?.contains(UserFlag.VerifiedBot) == true)
                append(Emojis.APP_TAG_START_VERIFIED)
            else
                append(Emojis.APP_TAG_START)

            append(Emojis.APP_TAG_END)
        }
    }
}