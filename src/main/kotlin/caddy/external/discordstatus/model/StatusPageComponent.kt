package caddy.external.discordstatus.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/*
    "id": "8r4xqnt9jslk",
      "name": "Android",
      "status": "operational",
      "created_at": "2023-12-13T14:41:56.266-08:00",
      "updated_at": "2023-12-13T14:42:16.513-08:00",
      "position": 3,
      "description": "The Android native client",
      "showcase": true,
      "start_date": "2023-12-13",
      "group_id": "fvcnrxvfw8f6",
      "page_id": "srhpyqt94yxb",
      "group": false,
      "only_show_if_degraded": false
 */
@Serializable
data class StatusPageComponent(
    val id: String,
    val name: String,
    val status: StatusPageComponentStatus,
    val createdAt: Instant,
    val updatedAt: Instant,
    val position: Int,
    val description: String?,
    val showcase: Boolean,
    val startDate: String?,
    val groupId: String?,
    val pageId: String,
    val group: Boolean,
    val onlyShowIfDegraded: Boolean,
    val components: List<String>? = null
)