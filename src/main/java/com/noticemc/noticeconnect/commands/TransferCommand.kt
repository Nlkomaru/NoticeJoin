package com.noticemc.noticeconnect.commands

import com.noticemc.noticeconnect.NoticeConnect
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
            actor.error("Player is already on that server!")
            return
        }
        val before = player.currentServer
        NoticeConnect.proxy.getPlayer(player.uniqueId).get().createConnectionRequest(server).fireAndForget()
        actor.reply("Transferred ${player.username} from ${before.get().serverInfo.name} to ${server.serverInfo.name}")
    }
}