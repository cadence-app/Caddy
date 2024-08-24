package caddy.db

import caddy.db.table.Cases
import caddy.db.util.KtormLogger
import caddy.db.util.execSqlScript
import org.ktorm.database.Database
import org.ktorm.entity.sequenceOf
import org.ktorm.logging.LogLevel
import org.ktorm.support.sqlite.SQLiteDialect

lateinit var CaddyDB: Database
    private set

val Database.cases get() = sequenceOf(Cases)

fun setupDb() {
    CaddyDB = Database.connect(
        url = "jdbc:sqlite:${System.getenv("CADDY_DB_LOCATION")}",
        driver = "org.sqlite.JDBC",
        dialect = SQLiteDialect(),
        logger = KtormLogger(LogLevel.TRACE)
    )

    CaddyDB.execSqlScript("init.sql")
}