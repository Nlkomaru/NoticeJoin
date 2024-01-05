/*
 * NoticeConnect-A login message plugin that runs on Velocity
 *
 * Written in 2021-2024  by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.
 * This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package com.noticemc.noticeconnect.commands

import com.noticemc.noticeconnect.NoticeConnect
import com.noticemc.noticeconnect.NoticeConnect.Companion.logger
import com.noticemc.noticeconnect.database.Database
import com.noticemc.noticeconnect.files.CustomConfig
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Description
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.velocity.annotation.CommandPermission
import revxrsal.commands.velocity.core.VelocityActor
import java.nio.file.Path

@Command("noticeconnect" ,"ntc")
@CommandPermission("noticeconnect.reload")
class ReloadCommand {
    var sql: Database? = null
    private val dir: Path = NoticeConnect.path

    @Subcommand("reload")
    @Description("Reload NoticeConnect")
    fun reloadCommand(actor :VelocityActor) {
        actor.reply("Reloading...")
        val config = CustomConfig()
        config.getConfigFile(dir)
        sqlConnection()
        actor.reply("Reloaded!")
    }

    private fun sqlConnection() {
        sql = Database()
        try {
            sql!!.connect(dir)
        } catch (e: Exception) {
            logger.error("Failed to connect to database")
            e.printStackTrace()
        }
        if (Database.isConnected()) {
            logger.info("Connected to database")
        }
        Database.initializeDatabase()
    }
}