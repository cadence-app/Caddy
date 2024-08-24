package caddy.db.table

import caddy.db.entity.Case
import caddy.db.entity.CaseType
import caddy.db.util.instant
import org.ktorm.schema.*

object Cases: Table<Case>("t_cases") {
    val id = int("id").primaryKey().bindTo { it.id }
    val type = enum<CaseType>("type").bindTo { it.type }
    val actorId = varchar("actor_id").bindTo { it.actorId }
    val targetId = varchar("target_id").bindTo { it.targetId }
    val reason = varchar("reason").bindTo { it.reason }
    val createdAt = instant("created_at").bindTo { it.createdAt }
}