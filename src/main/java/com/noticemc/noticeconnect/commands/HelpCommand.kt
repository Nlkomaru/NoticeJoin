/*
 * NoticeConnect-A login message plugin that runs on Velocity
 *
 * Written in 2021-2024  by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.
 * This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package com.noticemc.noticeconnect.commands

import com.noticemc.noticeconnect.utils.Utils.toGsonText
import net.kyori.adventure.text.minimessage.MiniMessage
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Default
import revxrsal.commands.annotation.Description
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.command.CommandActor
import revxrsal.commands.help.CommandHelp


@Command("noticeconnect", "ntc")
class HelpCommand {
    @Subcommand("help")
    @Description("Show help for NoticeConnect")
    fun help(actor: CommandActor, helpEntries: CommandHelp<String>, @Default("1") page: Int) {
        for (entry in helpEntries.paginate(page, 7)) {
            val mm = MiniMessage.miniMessage()
            val message = mm.deserialize(entry).toGsonText()
            actor.reply(message)
        }
    }
}