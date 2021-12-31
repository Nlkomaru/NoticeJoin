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
import com.noticemc.noticeconnect.database.Database;
import com.noticemc.noticeconnect.files.CustomConfig;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.UUID;

public class PlayerJoinEvent {


    @Subscribe
    public void onJoin(ServerPostConnectEvent event) {

        Player player = event.getPlayer();
        String serverName = player.getCurrentServer().get().getServerInfo().getName();
        final ProxyServer proxyServer = NoticeConnect.getProxy();
        if (event.getPreviousServer() == null) {

            if (playerExists(player.getUniqueId())) {
                String loginMessage = CustomConfig.getConfig().node("message", "join").getString();
                proxyServer.sendMessage(MiniMessage.get()
                        .parse(Objects.requireNonNull(loginMessage), Template.of("name", player.getUsername()),
                                Template.of("currentServerName", serverName)));
            } else {
                String loginMessage = CustomConfig.getConfig().node("message", "firstJoin").getString();
                proxyServer.sendMessage(MiniMessage.get()
                        .parse(Objects.requireNonNull(loginMessage), Template.of("name", player.getUsername()),
                                Template.of("currentServerName", serverName)));
                addPlayerLoginData(player.getUniqueId());
            }
        }
    }


    private boolean playerExists(UUID uuid) {
        boolean playerExists = false;
        try {
            Connection connection = Database.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM JoinedPlayerList WHERE uuid = ?");
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                playerExists = true;
            }
            rs.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playerExists;
    }

    private void addPlayerLoginData(UUID uuid) {
        try {
            Connection connection = Database.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO JoinedPlayerList (uuid) VALUES (?)");
            statement.setString(1, uuid.toString());
            statement.execute();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
