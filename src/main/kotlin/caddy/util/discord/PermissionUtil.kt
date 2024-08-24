package caddy.util.discord

import dev.kord.common.entity.Permission
import dev.kord.common.entity.Permission.*

val KeyPermissions = Administrator + KickMembers + BanMembers + ModerateMembers + ManageChannels + ManageGuild + ManageMessages + ManageGuildExpressions + ViewGuildInsights + ViewAuditLog + ManageNicknames + MentionEveryone + CreateGuildExpressions

fun Permission.getName(): String {
    return when (this) {
        CreateInstantInvite -> "Create Invite"
        KickMembers -> "Kick Members"
        BanMembers -> "Ban Members"
        Administrator -> "Administrator"
        ManageChannels -> "Manage Channels"
        ManageGuild -> "Manage Server"
        AddReactions -> "Add Reactions"
        ViewAuditLog -> "View Audit Log"
        PrioritySpeaker -> "Priority Speaker"
        Stream -> "Video"
        ViewChannel -> "View Channels"
        SendMessages -> "Send Messages"
        SendTTSMessages -> "Send Text-to-Speech Messages"
        ManageMessages -> "Manage Messages"
        EmbedLinks -> "Embed Links"
        AttachFiles -> "Attach Files"
        ReadMessageHistory -> "Read Message History"
        MentionEveryone -> "Mention \\@everyone"
        UseExternalEmojis -> "Use External Emojis"
        ViewGuildInsights -> "View Server Insights"
        Connect -> "Connect"
        Speak -> "Speak"
        MuteMembers -> "Mute Members"
        DeafenMembers -> "Deafen Members"
        MoveMembers -> "Move Members"
        UseVAD -> "Use Voice Activities"
        ChangeNickname -> "Change Nickname"
        ManageNicknames -> "Manage Nicknames"
        ManageRoles -> "Manage Roles"
        ManageWebhooks -> "Manage Webhooks"
        ManageGuildExpressions -> "Manage Emoji and Stickers"
        UseApplicationCommands -> "Use Application Commands"
        RequestToSpeak -> "Request to Speak"
        ManageEvents -> "Manage Events"
        ManageThreads -> "Manage Threads"
        CreatePublicThreads -> "Create Public Threads"
        CreatePrivateThreads -> "Create Private Threads"
        UseExternalStickers -> "Use External Stickers"
        SendMessagesInThreads -> "Send Messages in Threads"
        UseEmbeddedActivities -> "Use Activities"
        ModerateMembers -> "Timeout Members"
        ViewCreatorMonetizationAnalytics -> "View Creator Monetization Analytics"
        UseSoundboard -> "Use Soundboard"
        CreateGuildExpressions -> "Create Emoji and Stickers"
        CreateEvents -> "Create Events"
        UseExternalSounds -> "Use External Sounds"
        SendVoiceMessages -> "Send Voice Messages"
        Permission.fromShift(47) -> "Use Clyde AI"
        Permission.fromShift(48) -> "Set Voice Channel Status"
        Permission.fromShift(49) -> "Send Polls"
        Permission.fromShift(50) -> "Use External Apps"

        else -> "Unknown"
    }
}