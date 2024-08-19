package caddy.util

import com.xenomachina.argparser.ArgParser
import dev.kord.common.entity.Snowflake

private val userMentionRegex = "(<@!?)?([0-9]+)>?".toRegex()

fun ArgParser.userMentionPositional() = positional("") {
    Snowflake(replace(userMentionRegex, "$2"))
}