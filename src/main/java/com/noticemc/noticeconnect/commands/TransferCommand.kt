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
import com.noticemc.noticeconnect.utils.command.Suggestion
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.server.RegisteredServer
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Description
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.command.CommandActor
import revxrsal.commands.velocity.annotation.CommandPermission

@Command("noticeconnect", "ntc")
@CommandPermission("noticeconnect.transfer")
class TransferCommand {

    @Subcommand("transfer")
    @Description("Transfer a player to another server")
    fun transferCommand(
        actor: CommandActor,
        @Suggestion("server") server: RegisteredServer,
        @Suggestion("playerName") player: Player
    ) {
        if (player.currentServer.get().serverInfo.name == server.serverInfo.name) {
            actor.error(mm.deserialize("<red>Player is already on that server").toGsonText())
            return
        }
        NoticeConnect.proxy.getPlayer(player.uniqueId).get().createConnectionRequest(server).fireAndForget()
        actor.reply(mm.deserialize("<green>Transferred player to ${server.serverInfo.name}").toGsonText())
    }
}