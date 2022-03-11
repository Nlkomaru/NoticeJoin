/*
 *  <NoticeConnect>-<A login message plugin that runs on Velocity>
 *
 *  Written in 2021  by Nikomaru <nikomaru@nikomaru.dev>
 *
 *      To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the
 * public domain worldwide.
 *      This software is distributed without any warranty.
 *
 *      You should have received a copy of the CC0 Public Domain Dedication along with this software.
 *      If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package com.noticemc.noticeconnect.events

import com.noticemc.noticeconnect.NoticeConnect
import com.noticemc.noticeconnect.database.Database
import com.noticemc.noticeconnect.discord.SendDiscordChannel
import com.noticemc.noticeconnect.files.CustomConfig
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.ServerPostConnectEvent
import com.velocitypowered.api.proxy.ProxyServer
import net.dv8tion.jda.api.EmbedBuilder
import net.kyori.adventure.text.minimessage.MiniMessage
import org.spongepowered.configurate.kotlin.extensions.getList
import java.awt.Color
import java.sql.Connection
import java.util.*

class PlayerJoinEvent {
    @Subscribe
    fun onJoin(event: ServerPostConnectEvent) {
        val player = event.player
        if (!PlayerLoginEvent.list.remove(player.uniqueId)) {
            return
        }
        if (event.previousServer != null) {
            return
        }

        val mm = MiniMessage.miniMessage()
        val serverName = player.currentServer.get().serverInfo.name
        val proxyServer: ProxyServer = NoticeConnect.proxy!!

        val replaceName = CustomConfig.config.node("replace").node(serverName).string ?: serverName

        val eb = EmbedBuilder()
        val discordJoin: String?
        if (playerExists(player.uniqueId)) {
            val loginMessage: String? = CustomConfig.config.node("message", "join").getList(String::class)?.random()
            if (loginMessage != null && loginMessage != "" && !player.hasPermission("noticeconnect.hide.left")) {
                val replacedMessage = loginMessage.replace("<name>", player.username).replace("<currentServerName>", replaceName)
                proxyServer.sendMessage(mm.deserialize(replacedMessage))
            }
            discordJoin = (CustomConfig.config.node("discord", "message", "join").string)?.replace("%(PlayerName)", player.username)
            eb.setColor(Color.GREEN)
        } else {
            val loginMessage: String? = CustomConfig.config.node("message", "firstJoin").getList(String::class)?.random()
            if (loginMessage != null && loginMessage != "") {
                val replacedMessage = loginMessage.replace("<name>", player.username).replace("<currentServerName>", replaceName)
                proxyServer.sendMessage(mm.deserialize(replacedMessage))
                addPlayerLoginData(player.uniqueId)
            }
            discordJoin = (CustomConfig.config.node("discord", "message", "firstJoin").string)?.replace("%" + "(PlayerName)", player.username)
            eb.setColor(Color.YELLOW)
        }
        if (SendDiscordChannel.textChannel == null || discordJoin == "") {
            return
        }
        eb.setAuthor(discordJoin, null, "https://crafthead.net/avatar/" + player.uniqueId)
        SendDiscordChannel.textChannel!!.sendMessageEmbeds(eb.build()).queue()
    }

    private fun playerExists(uuid: UUID): Boolean {
        var playerExists = false
        try {
            val connection: Connection = Database.connection ?: return true
            val statement = connection.prepareStatement("SELECT * FROM JoinedPlayerList WHERE uuid = ?")
            statement.setString(1, uuid.toString())
            val rs = statement.executeQuery()
            if (rs.next()) {
                playerExists = true
            }
            rs.close()
            statement.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return playerExists
    }

    private fun addPlayerLoginData(uuid: UUID) {
        try {
            val connection: Connection? = Database.connection
            val statement = connection?.prepareStatement("INSERT INTO JoinedPlayerList (uuid) VALUES (?)")
            statement?.setString(1, uuid.toString())
            statement?.execute()
            statement?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}