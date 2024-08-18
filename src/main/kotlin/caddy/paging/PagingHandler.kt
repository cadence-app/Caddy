package caddy.paging

import caddy.util.Logger
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.entity.interaction.ButtonInteraction
import dev.kord.core.event.interaction.InteractionCreateEvent
import dev.kord.core.on

object PagingHandler {

    private val paginators = mutableMapOf<String, Paginator<*>>()

    private val logger = Logger("PagingHandler")

    operator fun <T> minusAssign(paginator: Paginator<T>) { paginators.remove(paginator.id) }
    operator fun <T> plusAssign(paginator: Paginator<T>) {
        paginators[paginator.id] = paginator
    }

    fun listen(kord: Kord) {
        kord.on<InteractionCreateEvent> {
            if (interaction is ButtonInteraction) {
                val id = interaction.data.data.customId.value

                if (id == null) {
                    logger.warn("Interaction customId missing")
                    kord.rest.interaction.createInteractionResponse(interaction.id, interaction.token, ephemeral = true) {
                        content = "Interaction unexpectedly failed"
                    }
                    return@on
                }

                if (id.startsWith("paging")) {
                    val (_, action, paginatorId) = id.split(":")
                    val paginator = paginators[paginatorId]

                    if (paginator == null) {
                        (interaction as ButtonInteraction).respondEphemeral {
                            content = "You can no longer page with this message"
                        }
                        return@on
                    }

                    if (interaction.user.id.toString() != paginator.userId) {
                        (interaction as ButtonInteraction).respondEphemeral {
                            content = "That's not for you :3"
                        }
                        return@on
                    }

                    kord.rest.interaction.deferMessageUpdate(interaction.id, interaction.token)
                    logger.debug(action)
                    when (action) {
                        "next" -> paginator.next()
                        "prev" -> paginator.prev()
                        "first" -> paginator.first()
                        "last" -> paginator.last()
                        "del" -> paginator.destroy()

                        else -> (interaction as ButtonInteraction).respondEphemeral {
                            content = "Unknown paging action"
                        }
                    }
                }
            }
        }

    }

}