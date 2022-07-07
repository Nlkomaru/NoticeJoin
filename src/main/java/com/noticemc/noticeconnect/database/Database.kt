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
package com.noticemc.noticeconnect.database

import com.noticemc.noticeconnect.files.CustomConfig
import java.io.File
import java.nio.file.Path
import java.sql.*

class Database {

    fun connect(dataDirectory: Path?) {
        if (!isConnected()) {
            Class.forName("org.sqlite.JDBC")
            val file = File(dataDirectory.toString(), CustomConfig.config.database.database + ".db")
            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs()
            }
            connection = DriverManager.getConnection("jdbc:sqlite:" + file.path)
        }
    }

    companion object {
        var connection: Connection? = null
        fun initializeDatabase() {
            try {
                val preparedStatement = connection?.prepareStatement("CREATE TABLE IF NOT EXISTS JoinedPlayerList (UUID VARCHAR(40) NOT NULL)")
                preparedStatement?.execute()
                preparedStatement?.close()
            } catch (ex: SQLException) {
                ex.printStackTrace()
            }
        }

        fun isConnected(): Boolean {
            return connection != null
        }

        fun disconnect() {
            if (isConnected()) {
                try {
                    connection?.close()
                } catch (e: SQLException) {
                    e.printStackTrace()
                }
            }
        }
    }
}