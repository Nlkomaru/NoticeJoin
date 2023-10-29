package com.noticemc.noticeconnect.commands

import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Default
import revxrsal.commands.annotation.Description
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.command.CommandActor
import revxrsal.commands.help.CommandHelp


@Command("noticeconnect", "ne")
class HelpCommand {
    @Subcommand("help")
    @Description("Show help for NoticeConnect")
    fun help(actor: CommandActor, helpEntries: CommandHelp<String>, @Default("1") page: Int) {
        for (entry in helpEntries.paginate(page, 7)) {
            val mm = MiniMessage.miniMessage()
            val message = mm.deserialize(entry)
            val legacy = LegacyComponentSerializer.legacySection().serialize(message)
            actor.reply(legacy)
        }
    }
}