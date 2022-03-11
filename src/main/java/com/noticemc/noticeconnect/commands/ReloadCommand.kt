/*
 *  <NoticeConnect>-<A login message plugin that runs on Velocity>
 *
 *  Written in 2022  by Nikomaru <nikomaru@nikomaru.dev>
 *
 *      To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the
 * public domain worldwide.
 *      This software is distributed without any warranty.
 *
 *      You should have received a copy of the CC0 Public Domain Dedication along with this software.
 *      If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package com.noticemc.noticeconnect.commands

import com.noticemc.noticeconnect.NoticeConnect
import com.noticemc.noticeconnect.database.Database
import com.noticemc.noticeconnect.discord.SendDiscordChannel
import com.noticemc.noticeconnect.files.CustomConfig
import com.velocitypowered.api.command.SimpleCommand
import org.slf4j.Logger
import java.nio.file.Path

class ReloadCommand : SimpleCommand {
    var sql: Database? = null
    private val logger: Logger? = NoticeConnect.logger
    private val dir: Path? = NoticeConnect.path
    override fun execute(invocation: SimpleCommand.Invocation) {
        logger!!.info("Reloading...")
        val config = CustomConfig()
        config.getConfigFile(dir)
        sqlConnection()
        if (!(CustomConfig.config.node("discord", "token").string == "" || CustomConfig.config.node("discord", "channel-id").string == "")) {
            SendDiscordChannel()
        }
        logger.info("NoticeConnectを再読み込みしました")
    }

    override fun hasPermission(invocation: SimpleCommand.Invocation): Boolean {
        return invocation.source().hasPermission("noticeconnect.reload")
    }

    private fun sqlConnection() {
        sql = Database()
        try {
            sql!!.connect(dir)
        } catch (e: Exception) {
            logger!!.error("Failed to connect to database")
            e.printStackTrace()
        }
        if (Database.isConnected()) {
            logger!!.info("Connected to database")
        }
        Database.initializeDatabase()
    }
}