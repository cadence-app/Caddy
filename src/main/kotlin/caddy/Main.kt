package caddy

import caddy.command.CommandHandler
import caddy.util.Logger
import dev.kord.core.Kord
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.on
import dev.kord.gateway.ALL
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent

@OptIn(PrivilegedIntent::class)
suspend fun main() {
    val client = Kord(System.getenv("CADDY_BOT_TOKEN")) {
        httpClient = LoggedHttpClient
    }

    client.on<ReadyEvent> {
        Logger.DEFAULT.info("Logged in as ${self.tag}")

        client.editPresence {
            watching("you.")
        }
    }

    CommandHandler.listen(client)

    client.login {
        intents = Intents.ALL
    }
}