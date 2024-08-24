package caddy.util.constants

val emojiMentionRx = "<:([A-z_0-9]+):(\\d+)>".toRegex()

object Emojis {

    const val GITHUB = "<:_:1274860271084765298>"
    const val ERROR = "<:X:1274861107844219022>"
    const val INFO = "<:i:1274866248668418089>"
    const val CHECK = "<:y:1276336871131189289>"
    const val SHIELD = "<:_:1276729607927627776>"

    // Badges
    const val HYPESQUAD_EVENTS = "<:_:1274943724002213939>"
    const val BRILLIANCE = "<:_:1274950448578695188>"
    const val BRAVERY = "<:_:1274950629881806931>"
    const val BALANCE = "<:_:1274950777835884657>"
    const val DISCORD_STAFF = "<:_:1274950919372410943>"
    const val PARTNER = "<:_:1274951088390406264>"
    const val MOD_PROGRAM_ALUMNI = "<:_:1274951207177289728>"
    const val VERIFIED_DEV = "<:_:1274951385065984022>"
    const val ACTIVE_DEV = "<:_:1274951548765732884>"
    const val EARLY_SUPPORTER = "<:_:1274946469392027709>"
    const val BUG_HUNTER_1 = "<:_:1274951720501248044>"
    const val BUG_HUNTER_2 = "<:_:1274951945001631774>"

    // App Tag
    const val APP_TAG_START = "<:_:1275182454860415087>"
    const val APP_TAG_START_VERIFIED = "<:_:1275183767622909972>"
    const val APP_TAG_END = "<:_:1275182472367181935>"

    // Logger Icons
    const val MEMBER_UPDATE = "<:_:1276363673442713631>"
    const val MEMBER_ADD = "<:_:1276364312352653352>"
    const val MEMBER_REMOVE = "<:_:1276364621325799424>"
    const val WARN = "<:w:1276684046830669947>"

    fun getId(emoji: String): String {
        return emoji.replace(emojiMentionRx, "$2")
    }

    fun getUrl(emoji: String): String {
        return "https://cdn.discordapp.com/emojis/${getId(emoji)}.webp"
    }

}