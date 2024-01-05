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
package com.noticemc.noticeconnect.events

import com.noticemc.noticeconnect.database.Database
import com.noticemc.noticeconnect.discord.Author
import com.noticemc.noticeconnect.discord.DiscordWebhookData
import com.noticemc.noticeconnect.discord.DiscordWebhookEmbed
import com.noticemc.noticeconnect.files.CustomConfig
import com.noticemc.noticeconnect.utils.Utils.mm
import com.noticemc.noticeconnect.utils.Utils.sendAudienceMessage
import com.noticemc.noticeconnect.utils.Utils.sendWebHook
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.ServerPostConnectEvent
import org.apache.commons.lang3.StringUtils
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

        val serverName = player.currentServer.get().serverInfo.name
        val replaceName = CustomConfig.config.replace[serverName] ?: serverName

        lateinit var discordJoin: String
        val color: Int

        if (playerExists(player.uniqueId)) {
            val loginMessage: String = CustomConfig.config.message.join.random()

            if (!StringUtils.isBlank(loginMessage) && !player.hasPermission("noticeconnect.hide.left")) {
                val replacedMessage = loginMessage.replace("<name>", player.username).replace("<currentServerName>", replaceName)
                sendAudienceMessage(mm.deserialize(replacedMessage))
            }
            color = Integer.valueOf(Integer.toHexString(Color.GREEN.rgb).substring(2), 16)
            discordJoin = CustomConfig.config.discord.message.join.replace("%(PlayerName)", player.username)
        } else {
            val loginMessage: String = CustomConfig.config.message.firstJoin.random()
            if (!StringUtils.isBlank(loginMessage)) {
                val replacedMessage = loginMessage.replace("<name>", player.username).replace("<currentServerName>", replaceName)

                sendAudienceMessage(mm.deserialize(replacedMessage))
                addPlayerLoginData(player.uniqueId)

            }
            color = Integer.valueOf(Integer.toHexString(Color.YELLOW.rgb).substring(2), 16)
            discordJoin = (CustomConfig.config.discord.message.firstJoin).replace("%" + "(PlayerName)", player.username)
        }
        if (discordJoin.isBlank()) {
            return
        }
        val author = Author(discordJoin, null, "https://crafthead.net/avatar/" + player.uniqueId)
        val embed = DiscordWebhookEmbed(color, author)

        DiscordWebhookData(arrayListOf(embed)).sendWebHook()

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