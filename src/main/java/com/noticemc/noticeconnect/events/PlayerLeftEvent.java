/*
 *  <NoticeConnect>-<A login message plugin that runs on Velocity>
 *
 *  Written in 2021  by Nikomaru <nikomaru@nikomaru.dev>
 *
 *      To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.
 *      This software is distributed without any warranty.
 *
 *      You should have received a copy of the CC0 Public Domain Dedication along with this software.
 *      If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package com.noticemc.noticeconnect.events;

import com.noticemc.noticeconnect.NoticeConnect;
import com.noticemc.noticeconnect.files.CustomConfig;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;

import java.util.Objects;

public class PlayerLeftEvent {
    @Subscribe
    public void onLeft(DisconnectEvent event) {
        Player player = event.getPlayer();
        if (player.getCurrentServer().isEmpty()) {
            return;
        }
        String serverName = player.getCurrentServer().get().getServerInfo().getName();
        final ProxyServer proxyServer = NoticeConnect.getProxy();
        if (serverName == null) {
            serverName = "Unknown";
        }

        String loginMessage = CustomConfig.getConfig().node("message", "left").getString();
        proxyServer.sendMessage(MiniMessage.get()
                .parse(Objects.requireNonNull(loginMessage), Template.of("name", player.getUsername()),
                        Template.of("currentServerName", serverName)));
    }
}
