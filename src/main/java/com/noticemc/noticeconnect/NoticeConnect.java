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

package com.noticemc.noticeconnect;

import com.google.inject.Inject;
import com.noticemc.noticeconnect.database.Database;
import com.noticemc.noticeconnect.events.PlayerJoinEvent;
import com.noticemc.noticeconnect.events.PlayerLeftEvent;
import com.noticemc.noticeconnect.files.CustomConfig;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(id = "noticeconnect", name = "NoticeConnect", version = "1.0-SNAPSHOT", authors = {"Nikomaru"})
public class NoticeConnect {
    private static ProxyServer proxyServer = null;
    Database sql = null;
    private Logger logger;
    private Path dir;

    public static ProxyServer getProxy() {
        return proxyServer;
    }

    @Inject
    public void noticeConnect(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        proxyServer = server;
        this.logger = logger;
        dir = dataDirectory;
        CustomConfig config = new CustomConfig();
        config.getConfigFile(dataDirectory);
        sqlConnection();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        proxyServer.getEventManager().register(this, new PlayerJoinEvent());
        proxyServer.getEventManager().register(this, new PlayerLeftEvent());
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
