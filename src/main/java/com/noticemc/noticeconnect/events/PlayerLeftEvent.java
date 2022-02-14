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

package com.noticemc.noticeconnect.events;

import com.noticemc.noticeconnect.NoticeConnect;
import com.noticemc.noticeconnect.discord.SendDiscordChannel;
import com.noticemc.noticeconnect.files.CustomConfig;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;

import java.awt.Color;
import java.util.Objects;

public class PlayerLeftEvent {

    @Subscribe
    public void onLeft(DisconnectEvent event) {
        Player player = event.getPlayer();
        if(player.hasPermission("noticeconnect.hide.left")) return;

        if (player.getCurrentServer().isEmpty()) {
            return;
        }
        String serverName = player.getCurrentServer().get().getServerInfo().getName();
        final ProxyServer proxyServer = NoticeConnect.getProxy();
        if (serverName == null) {
            serverName = "server";
        }
        if (CustomConfig.getConfig().node("replace").node(serverName).getString() != null) {
            serverName = CustomConfig.getConfig().node("replace").node(serverName).getString();
        }

        String leftMessage = CustomConfig.getConfig().node("message", "left").getString();
        if (leftMessage != null && !leftMessage.equals("")) {
            proxyServer.sendMessage(MiniMessage.get()
                    .parse(Objects.requireNonNull(leftMessage), Template.of("name", player.getUsername()),
                            Template.of("currentServerName", Objects.requireNonNull(serverName))));
        }

        if (SendDiscordChannel.getTextChannel() == null) {
            return;
        }

        String discordLeftMessage =
                (Objects.requireNonNull(CustomConfig.getConfig().node("discord", "message", "left").getString())).replace("%(PlayerName)",
                        player.getUsername());
        if (discordLeftMessage.equals("")) {
            return;
        }

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.RED);
        eb.setAuthor(discordLeftMessage, null, "https://crafthead.net/avatar/" + player.getUniqueId());
        SendDiscordChannel.getTextChannel().sendMessageEmbeds(eb.build()).queue();
    }
}
