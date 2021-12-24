/*
 * <NoticeJoin>-<A login message plugin that runs on Velocity>
 *
 * Written in 2021  by Nikomaru <nikomaru@nikomaru.dev>
 *
 *     To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.
 *     This software is distributed without any warranty.
 *
 *     You should have received a copy of the CC0 Public Domain Dedication along with this software.
 *     If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package com.noticemc.noticejoin.events;

import com.noticemc.noticejoin.NoticeJoin;
import com.noticemc.noticejoin.files.CustomConfig;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.format.TextColor;

import java.util.Objects;

import static net.kyori.adventure.text.Component.text;

public class PlayerLeftEvent {
    @Subscribe
    public void onLeft(DisconnectEvent event) {
        Player player = event.getPlayer();
        if(player.getCurrentServer().isEmpty()){
            return;
        }
        String leftServer = player.getCurrentServer().get().getServerInfo().getName();
        final ProxyServer proxyServer = NoticeJoin.getProxy();
        if(leftServer == null) {
            leftServer = "Unknown";
        }

        proxyServer.sendMessage((text(player.getUsername(), TextColor.fromHexString("#fba700"))).append(
                        text("さんが", TextColor.fromHexString("#fbfb54"))).append(text(
                        Objects.requireNonNull(CustomConfig.getConfig().getNode("server").getNode("name").getString()),
                        TextColor.fromHexString("#fba700")))
                .append(text("(" + leftServer + ")", TextColor.fromHexString("#a8a7a8")))
                .append(text("から離れました", TextColor.fromHexString("#fbfb54"))));
    }
}
