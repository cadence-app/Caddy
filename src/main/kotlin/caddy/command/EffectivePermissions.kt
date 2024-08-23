package caddy.command

import dev.kord.common.entity.Permission

/*
    We use our own set of permissions instead of discords in order to limit
    how many perms a role actually has.
 */

// SELFHOSTERS: Set up your own role perms here


private const val TEAM = "1214393147694645348"

val EffectivePermissions = mapOf(
    TEAM to listOf(Permission.ManageRoles, Permission.ModerateMembers, Permission.BanMembers, Permission.KickMembers)
)