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

import com.noticemc.noticeconnect.Utils.mm
import com.noticemc.noticeconnect.Utils.sendAudienceMessage
import com.noticemc.noticeconnect.Utils.sendWebHook
import com.noticemc.noticeconnect.discord.*
import com.noticemc.noticeconnect.files.CustomConfig
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent
import org.apache.commons.lang3.StringUtils
import java.awt.Color

class PlayerLeftEvent {
    @Subscribe
    fun onLeft(event: DisconnectEvent) {
        val player = event.player
        if (player.currentServer.isEmpty) {
            return
        }

        val serverName = player.currentServer.get().serverInfo.name ?: "server"

        val replaceName = CustomConfig.config.replace[serverName] ?: serverName

        val leftMessage: String = CustomConfig.config.message.left.random()

        if (StringUtils.isBlank(leftMessage) && !player.hasPermission("noticeconnect.hide.left")) {
            val replacedMessage = leftMessage.replace("<name>", player.username).replace("<currentServerName>", replaceName)

            sendAudienceMessage(mm.deserialize(replacedMessage))
        }

        val discordLeftMessage = CustomConfig.config.discord.message.left.replace("%(PlayerName)", player.username)
        if (discordLeftMessage.isBlank()) {
            return
        }
        val author = Author(discordLeftMessage, null, "https://crafthead.net/avatar/" + player.uniqueId)
        val embed = DiscordWebhookEmbed(Integer.valueOf(Integer.toHexString(Color.RED.rgb).substring(2), 16), author)

        DiscordWebhookData(arrayListOf(embed)).sendWebHook()

    }
}
