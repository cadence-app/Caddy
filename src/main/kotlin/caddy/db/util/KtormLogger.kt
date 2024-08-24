package caddy.db.util

import org.ktorm.logging.LogLevel
import org.ktorm.logging.Logger

class KtormLogger(private val threshold: LogLevel) : Logger {

    private val _logger = caddy.util.Logger("SQL")

    override fun isTraceEnabled(): Boolean {
        return LogLevel.TRACE >= threshold
    }

    override fun trace(msg: String, e: Throwable?) {
        _logger.debug(msg, e)
    }

    override fun isDebugEnabled(): Boolean {
        return LogLevel.DEBUG >= threshold
    }

    override fun debug(msg: String, e: Throwable?) {
        _logger.debug(msg, e)
    }

    override fun isInfoEnabled(): Boolean {
        return LogLevel.INFO >= threshold
    }

    override fun info(msg: String, e: Throwable?) {
        _logger.info(msg, e)
    }

    override fun isWarnEnabled(): Boolean {
        return LogLevel.WARN >= threshold
    }

    override fun warn(msg: String, e: Throwable?) {
        _logger.warn(msg, e)
    }

    override fun isErrorEnabled(): Boolean {
        return LogLevel.ERROR >= threshold
    }

    override fun error(msg: String, e: Throwable?) {
        _logger.error(msg, e)
    }
}