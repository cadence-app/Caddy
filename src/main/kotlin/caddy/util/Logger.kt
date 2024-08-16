package caddy.util

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.*

class Logger(
    private val tag: String
) {

    private val json = Json {
        prettyPrint = true
    }

    private fun timestamp() = SimpleDateFormat("M/d/y, h:mm:ss a", Locale.getDefault()).format(Date(System.currentTimeMillis()))

    private fun write(color: Color, message: String) {
        println("${color.code}$message${Color.RESET.code}")
    }

    private fun indent(text: String, indent: Int): String {
        return text
            .split("\n")
            .joinToString("\n") { " ".repeat(indent) + it }
    }

    private fun log(level: Level, message: String, throwable: Throwable?, obj: Any?) {
        val prefix = "${timestamp()} [${level.name}] [$tag] "

        val formattedMessage = message
            .split("\n")
            .mapIndexed { index, s ->
                if (index != 0) indent(s, prefix.length) else s
            }
            .joinToString("\n")

        val msg = buildString {
            appendLine("$prefix$formattedMessage")
            if (throwable != null) appendLine(indent(throwable.stackTraceToString(), prefix.length))
            if (obj != null) appendLine(indent(json.encodeToString(obj), prefix.length))
        }

        write(level.color, msg.trim())
    }

    fun debug(message: String, throwable: Throwable? = null, obj: Any? = null) = log(Level.DEBUG, message, throwable, obj)
    fun info(message: String, throwable: Throwable? = null, obj: Any? = null) = log(Level.INFO, message, throwable, obj)
    fun warn(message: String, throwable: Throwable? = null, obj: Any? = null) = log(Level.WARN, message, throwable, obj)
    fun error(message: String, throwable: Throwable? = null, obj: Any? = null) = log(Level.ERROR, message, throwable, obj)

    companion object {

        val DEFAULT = Logger("Caddy")

    }

}

private enum class Color(val code: String) {
    ERROR("\u001b[31m"),
    WARN("\u001b[33m"),
    INFO("\u001b[36m"),
    DEBUG("\u001b[37m"),
    RESET("\u001b[0m")
}

private enum class Level(val color: Color) {
    DEBUG(Color.DEBUG),
    INFO(Color.INFO),
    WARN(Color.WARN),
    ERROR(Color.ERROR)
}