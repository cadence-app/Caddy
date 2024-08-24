package caddy.db.util

import kotlinx.datetime.Instant
import kotlinx.datetime.toKotlinInstant
import org.ktorm.database.Database
import org.ktorm.database.use
import org.ktorm.schema.BaseTable
import org.ktorm.schema.Column
import org.ktorm.schema.SqlType
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Timestamp
import java.sql.Types

fun BaseTable<*>.instant(name: String): Column<Instant> {
    return registerColumn(name, InstantSqlType)
}

object InstantSqlType : SqlType<Instant>(Types.TIMESTAMP, "timestamp") {

    override fun doSetParameter(ps: PreparedStatement, index: Int, parameter: Instant) {
        ps.setTimestamp(index, Timestamp(parameter.toEpochMilliseconds()))
    }

    override fun doGetResult(rs: ResultSet, index: Int): Instant? {
        return rs.getTimestamp(index)?.toInstant()?.toKotlinInstant()
    }
}

fun Database.execSqlScript(filename: String) {
    useConnection { conn ->
        conn.createStatement().use { statement ->
            javaClass.classLoader
                ?.getResourceAsStream(filename)
                ?.bufferedReader()
                ?.use { reader ->
                    for (sql in reader.readText().split(';')) {
                        if (sql.any { it.isLetterOrDigit() }) {
                            statement.executeUpdate(sql)
                        }
                    }
                }
        }
    }
}