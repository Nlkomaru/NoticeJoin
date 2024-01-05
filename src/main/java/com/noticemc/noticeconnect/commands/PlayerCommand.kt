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

import com.noticemc.noticeconnect.NoticeConnect
import com.noticemc.noticeconnect.utils.Utils.mm
import com.noticemc.noticeconnect.utils.Utils.toGsonText
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.velocity.annotation.CommandPermission
import revxrsal.commands.velocity.core.VelocityActor

@Command("noticeconnect" ,"ntc")
@CommandPermission("noticeconnect.player")
class PlayerCommand {
    @Subcommand("player")
    fun playerCommand(actor : VelocityActor) {
        val proxy = NoticeConnect.proxy
        val servers = proxy.allServers
        servers.forEach{ server ->
            val serverName = server.serverInfo.name
            val players = server.playersConnected.joinToString(", ") { it.username }
            val message = "$serverName $players"
            actor.reply(message)
        }
    }
}
