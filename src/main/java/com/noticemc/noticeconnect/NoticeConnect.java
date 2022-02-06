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

package com.noticemc.noticeconnect;

import com.google.inject.Inject;
import com.noticemc.noticeconnect.commands.ReloadCommand;
import com.noticemc.noticeconnect.database.Database;
import com.noticemc.noticeconnect.discord.SendDiscordChannel;
import com.noticemc.noticeconnect.events.PlayerJoinEvent;
import com.noticemc.noticeconnect.events.PlayerLeftEvent;
import com.noticemc.noticeconnect.events.PlayerLoginEvent;
import com.noticemc.noticeconnect.files.CustomConfig;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Objects;

@Plugin(id = "noticeconnect", name = "NoticeConnect", version = "1.0-SNAPSHOT")
public class NoticeConnect {

    private static ProxyServer proxyServer = null;
    Database sql = null;
    private static Logger logger;
    private static Path dir;

    public static ProxyServer getProxy() {
        return proxyServer;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static Path getPath() {
        return dir;
    }

    @Inject
    public void noticeConnect(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        proxyServer = server;
        NoticeConnect.logger = logger;
        dir = dataDirectory;
        CustomConfig config = new CustomConfig();
        config.getConfigFile(dir);
        sqlConnection();
        logger.info("今までに" + getJoinedPlayerCount() + "人のプレイヤーがサーバーを訪れました");

        if (!(Objects.equals(CustomConfig.getConfig().node("discord", "token").getString(), "") || Objects.equals(
                CustomConfig.getConfig().node("discord", "channel-id").getString(), ""))) {
            new SendDiscordChannel();
        }
    }

    private int getJoinedPlayerCount() {
        int count = 0;
        try {
            Connection connection = Database.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM JoinedPlayerList");
            count = statement.executeQuery().getInt(1);
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        proxyServer.getEventManager().register(this, new PlayerJoinEvent());
        proxyServer.getEventManager().register(this, new PlayerLeftEvent());
        proxyServer.getEventManager().register(this, new PlayerLoginEvent());
        CommandManager commandManager = proxyServer.getCommandManager();
        CommandMeta meta = commandManager.metaBuilder("NoticeConnect").build();
        commandManager.register(meta, new ReloadCommand());
    }


    private void sqlConnection() {
        sql = new Database();
        try {
            sql.connect(dir);
        } catch (Exception e) {
            logger.error("Failed to connect to database");
            e.printStackTrace();
        }
        if (Database.isConnected()) {
            logger.info("Connected to database");
        }
        Database.initializeDatabase();
    }


}
