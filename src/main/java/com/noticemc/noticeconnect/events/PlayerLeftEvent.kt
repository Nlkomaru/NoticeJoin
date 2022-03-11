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
import com.noticemc.noticeconnect.discord.SendDiscordChannel
import com.noticemc.noticeconnect.files.CustomConfig
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent
import com.velocitypowered.api.proxy.ProxyServer
import net.dv8tion.jda.api.EmbedBuilder
import net.kyori.adventure.text.minimessage.MiniMessage
import org.spongepowered.configurate.kotlin.extensions.getList
import java.awt.Color

class PlayerLeftEvent {
    @Subscribe
    fun onLeft(event: DisconnectEvent) {
        val player = event.player
        if (player.currentServer.isEmpty) {
            return
        }
        val mm = MiniMessage.miniMessage()
        val serverName = player.currentServer.get().serverInfo.name ?: "server"
        val proxyServer: ProxyServer? = NoticeConnect.proxy

        val replaceName = CustomConfig.config.node("replace").node(serverName).string ?: serverName

        val leftMessage: String? = CustomConfig.config.node("message", "left").getList(String::class)?.random()

        if (leftMessage != null && leftMessage != "" && !player.hasPermission("noticeconnect.hide.left")) {
            val replacedMessage = leftMessage.replace("<name>", player.username).replace("<currentServerName>", replaceName)
            proxyServer?.sendMessage(mm.deserialize(replacedMessage))
        }

        SendDiscordChannel.textChannel ?: return

        val discordLeftMessage = (CustomConfig.config.node("discord", "message", "left").string)?.replace("%(PlayerName)", player.username)
        if (discordLeftMessage == "") {
            return
        }
        val eb = EmbedBuilder()
        eb.setColor(Color.RED)
        eb.setAuthor(discordLeftMessage, null, "https://crafthead.net/avatar/" + player.uniqueId)
        SendDiscordChannel.textChannel!!.sendMessageEmbeds(eb.build()).queue()
    }
}