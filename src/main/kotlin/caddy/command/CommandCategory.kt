package caddy.command

enum class CommandCategory(
    val title: String,
    val description: String
) {
    UTILITY("Utility", "Basic utility commands"),
    FUN("Fun", "Commands that don't server any actual purpose"),
    MODERATION("Moderation", "Commands used to moderate the server")
}