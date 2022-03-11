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
package com.noticemc.noticeconnect.files

import com.typesafe.config.ConfigRenderOptions
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.hocon.encodeToConfig
import org.spongepowered.configurate.CommentedConfigurationNode
import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import java.io.BufferedWriter
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.nio.file.Path
import kotlin.io.path.createFile
import kotlin.io.path.exists

object ObjectMapperExample {

    fun main(path: Path) {
        val ver = "1.0.0"
        val config = Config(ver,
            Message(listOf("<gray>[<yellow>初見さんいらっしゃい</yellow>] <light_purple><name>さんがはじめて<currentServerName>サーバーに入りました!"),
                listOf("<yellow><gold><name></gold>さんが<gold>サーバー<gold><gray>(<currentServerName>)</gray>にやってきました!"),
                listOf("<yellow><gold><name></gold>さんが<gold>サーバー<gold><gray>(<currentServerName>)</gray>から離れました")),
            Database("NoticeConnect"),
            HashMap(),
            Discord("", "", DiscordMessage("%(PlayerName) さんがサーバーに初参加です", "%(PlayerName) さんがサーバーに参加しました", "%(PlayerName) さんがサーバーから退出しました")))
        val encode = hocon.encodeToConfig(config)

        val renderOptions = ConfigRenderOptions.defaults().setOriginComments(false).setComments(false).setFormatted(true).setJson(false)
        val string = encode.root().render(renderOptions)
        if (!path.exists()) {
            path.createFile()
            val fw = PrintWriter(BufferedWriter(OutputStreamWriter(FileOutputStream(path.toFile()), "UTF-8")))
            fw.write(string)
            fw.close()
        } else {
            val verNode: CommentedConfigurationNode? = HoconConfigurationLoader.builder().path(path).build().load().node("version")
            if (verNode == null || verNode.string != ver) {
                path.toFile().deleteOnExit()
                val fw = PrintWriter(BufferedWriter(OutputStreamWriter(FileOutputStream(path.toFile()), "UTF-8")))
                fw.write(string)
                fw.close()
            }
        }
    }

}

@OptIn(ExperimentalSerializationApi::class)
private val hocon = Hocon

@Serializable
data class DiscordMessage(val firstJoin: String, val join: String, val left: String)

@Serializable
data class Discord(val token: String, val channel: String, val message: DiscordMessage)

@Serializable
data class Message(val firstJoin: List<String>, val join: List<String>, val left: List<String>)

@Serializable
data class Database(val database: String)

@Serializable
data class Config(val version: String, val message: Message, val database: Database, val replace: Map<String, String>, val discord: Discord)