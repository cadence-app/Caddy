package caddy.util

import com.xenomachina.argparser.ArgParser
import dev.kord.common.entity.Snowflake

private val userMentionRegex = "(<@!?)?([0-9]+)>?".toRegex()
val roleMentionRegex = "(<@&?)?([0-9]+)>?".toRegex()

fun ArgParser.userMentionPositional() = positional("") {
    runCatching { Snowflake(replace(userMentionRegex, "$2")) }.getOrNull()
}