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
package com.noticemc.noticeconnect

import com.google.inject.Inject
import com.noticemc.noticeconnect.commands.ReloadCommand
import com.noticemc.noticeconnect.database.Database
import com.noticemc.noticeconnect.events.*
import com.noticemc.noticeconnect.files.CustomConfig
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import org.slf4j.Logger
import java.nio.file.Path
import java.sql.Connection

class NoticeConnect {
    private var sql: Database? = null

    @Inject
    fun noticeConnect(server: ProxyServer, logger: Logger, @DataDirectory dataDirectory: Path) {
        proxy = server
        Companion.logger = logger
        path = dataDirectory
        CustomConfig().getConfigFile(path)
        sqlConnection()
        logger.info("今までに" + joinedPlayerCount() + "人のプレイヤーがサーバーを訪れました")

    }

    private fun joinedPlayerCount(): Int {
        var count = 0
        try {
            val connection: Connection? = Database.connection
            val statement = connection?.prepareStatement("SELECT COUNT(*) FROM JoinedPlayerList")
            count = statement?.executeQuery()?.getInt(1) ?: 0
            statement?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return count
    }

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent?) {
        proxy.eventManager?.register(this, PlayerJoinEvent())
        proxy.eventManager?.register(this, PlayerLeftEvent())
        proxy.eventManager?.register(this, PlayerLoginEvent())
        val commandManager = proxy.commandManager
        val meta = commandManager?.metaBuilder("NoticeConnect")?.build()
        commandManager?.register(meta, ReloadCommand())
    }

    private fun sqlConnection() {
        sql = Database()
        try {
            sql!!.connect(path)
        } catch (e: Exception) {
            logger.error("Failed to connect to database")
            e.printStackTrace()
        }
        if (Database.isConnected()) {
            logger.info("Connected to database")
        }
        Database.initializeDatabase()
    }

    companion object {
        lateinit var proxy: ProxyServer
        lateinit var logger: Logger
        lateinit var path: Path
    }
}
