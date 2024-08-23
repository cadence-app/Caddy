package caddy

import caddy.command.CommandHandler
import caddy.paging.PagingHandler
import caddy.util.Logger
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.on
import dev.kord.gateway.ALL
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent

var onlineTimestamp = 0L
    private set

var ownerId: Snowflake? = null
    private set

@OptIn(PrivilegedIntent::class)
suspend fun main() {
    val client = Kord(System.getenv("CADDY_BOT_TOKEN")) {
        httpClient = LoggedHttpClient
    }

    val appInfo = client.getApplicationInfo()
    ownerId = appInfo.team?.ownerUserId ?: appInfo.ownerId

    client.on<ReadyEvent> {
        Logger.DEFAULT.info("Logged in as ${self.tag}")
        onlineTimestamp = System.currentTimeMillis()

        client.editPresence {
            watching("you.")
        }
    }

    CommandHandler.listen(client)
    PagingHandler.listen(client)

    client.login {
        intents = Intents.ALL
    }
}