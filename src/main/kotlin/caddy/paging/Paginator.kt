package caddy.paging

import dev.kord.common.entity.ButtonStyle
import dev.kord.common.entity.DiscordPartialEmoji
import dev.kord.core.behavior.MessageBehavior
import dev.kord.core.behavior.edit
import dev.kord.core.behavior.reply
import dev.kord.core.entity.Message
import dev.kord.rest.builder.component.ActionRowBuilder
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.MessageBuilder
import dev.kord.rest.builder.message.actionRow
import dev.kord.rest.builder.message.modify.UserMessageModifyBuilder
import kotlinx.coroutines.*
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.math.ceil
import kotlin.random.Random
import kotlin.time.Duration.Companion.minutes

// Translation of https://codeberg.org/vee/bot/src/branch/main/src/util/Paginator.ts
open class Paginator<T>(
    val data: List<T>,
    val pageSize: Int = 10
) {

    @OptIn(ExperimentalEncodingApi::class)
    val id = Base64.encode(Random.nextInt(100000000, 999999999).toString().encodeToByteArray())
    val totalPages = ceil(data.size.toFloat() / pageSize).toInt()

    private var currentPage: Int = 0
    var message: Message? = null
    var userId: String = ""
        private set

    private var timeoutJob: Job? = null

    private val isFirstPage
        get() = currentPage == 0

    private val isLastPage
        get() = currentPage == totalPages - 1

    suspend fun create(targetMessage: Message) {
        destroy()
        message = targetMessage.reply {
            renderPage(0)
        }

        userId = targetMessage.author?.id?.toString() ?: "-1"
        PagingHandler += this

        timeoutJob = CoroutineScope(Dispatchers.IO).launch {
            delay(5.minutes)
            destroy()
        }
    }

    suspend fun destroy() {
        message?.let {
            it.edit { components = mutableListOf() }
            message = null
        }

        timeoutJob?.cancel()
        PagingHandler -= this
    }

    private suspend fun navigateToPage(page: Int) {
        currentPage = page
        message?.edit {
            renderPage(page)
        }
    }

    suspend fun next() {
        navigateToPage(currentPage + 1)
    }

    suspend fun prev() {
        navigateToPage(currentPage - 1)
    }

    suspend fun first() {
        navigateToPage(0)
    }

    suspend fun last() {
        navigateToPage(totalPages - 1)
    }

    private fun MessageBuilder.renderPage(page: Int) {
        embeds = mutableListOf(buildEmbed(page))
        if (!isFirstPage && !isLastPage) {
            actionRow {
                interactionButton(ButtonStyle.Secondary, "paging:first:$id") {
                    emoji = DiscordPartialEmoji(name = "⏪")
                    disabled = isFirstPage
                }

                interactionButton(ButtonStyle.Primary, "paging:prev:$id") {
                    emoji = DiscordPartialEmoji(name = "◀")
                    disabled = isFirstPage
                }

                interactionButton(ButtonStyle.Primary, "paging:next:$id") {
                    emoji = DiscordPartialEmoji(name = "▶")
                    disabled = isLastPage
                }

                interactionButton(ButtonStyle.Secondary, "paging:last:$id") {
                    emoji = DiscordPartialEmoji(name = "⏩")
                    disabled = isLastPage
                }

                interactionButton(ButtonStyle.Danger, "paging:del:$id") {
                    emoji = DiscordPartialEmoji(name = "🗑")
                }
            }
        }
    }

    open fun buildEmbed(page: Int): EmbedBuilder = EmbedBuilder()

}

suspend fun <T> Message.createPaginator(
    data: List<T>,
    pageSize: Int = 10,
    builder: EmbedBuilder.(page: List<T>) -> Unit
): Paginator<T> {
    val paginator = object : Paginator<T>(data, pageSize) {

        override fun buildEmbed(page: Int): EmbedBuilder {
            val pageData = data.chunked(pageSize).getOrNull(page)

            return EmbedBuilder().apply {
                footer {
                    text = "Page ${page + 1} of $totalPages"
                }

                builder(pageData ?: emptyList())
            }
        }

    }

    paginator.create(this)

    return paginator
}