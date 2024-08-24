package caddy.command.`fun`

import caddy.command.CommandCategory
import caddy.command.createCommand
import caddy.util.constants.Colors
import caddy.util.discord.replyEmbed
import dev.kord.core.behavior.edit
import dev.kord.rest.builder.message.embed
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.time.Duration.Companion.seconds

val Warm = createCommand(
    name = "warm",
    description = "Warms a user",
    usage = ":warm <target>",
    category = CommandCategory.FUN
) { event ->

    val targetList by positionalList("")

    val randomTime = Random.nextInt(1..10)

    val target = targetList.joinToString(" ")

    val msg = event.message.replyEmbed {
        color = Colors.Blue
        description = "Warming $target..."
    }

    delay(randomTime.seconds)

    msg.edit {
        embed {
            color = when(randomTime) {
                in 1..5 -> Colors.Yellow
                in 6..8 -> Colors.Blue
                else -> Colors.Red
            }

            description = when(randomTime) {
                in 1..5 -> "Looks like you didn't wait long enough, $target is too cold"
                in 6..8 -> "$target has been warmed to perfection!"
                else -> "Oops! You burned $target..."
            }
        }
    }
}