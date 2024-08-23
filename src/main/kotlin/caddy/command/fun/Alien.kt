package caddy.command.`fun`

import caddy.command.CommandCategory
import caddy.command.createCommand

val Alien = createCommand(
    name = "alien",
    description = "zoop beeble lorp",
    category = CommandCategory.FUN,
    aliases = listOf("👽", ":alien:", "dolfer", "dolfies", "zorp")
) { event ->
    event.message.channel.createMessage("👽👽👽👽")
}