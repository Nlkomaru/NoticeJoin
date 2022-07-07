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
import kotlinx.serialization.hocon.encodeToConfig
import org.spongepowered.configurate.CommentedConfigurationNode
import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import java.io.*
import java.nio.file.Path
import kotlin.io.path.createFile
import kotlin.io.path.exists

private const val ver = "2.0.0"
private const val firstMessage = "<gray>[<yellow>初見さんいらっしゃい</yellow>] <light_purple><name>さんがはじめて<currentServerName>サーバーに入りました!"
private const val joinMessage = "<yellow><gold><name></gold>さんが<gold>サーバー<gold><gray>(<currentServerName>)</gray>にやってきました!"
private const val leftMessage = "<yellow><gold><name></gold>さんが<gold>サーバー<gold><gray>(<currentServerName>)</gray>から離れました"
private const val discordFirstJoin = "%(PlayerName) さんがサーバーに初参加です"
private const val discordJoin = "%(PlayerName) さんがサーバーに参加しました"
private const val discordLeft = "%(PlayerName) さんがサーバーから退出しました"
private const val defaultMode = "Chat"
private const val databaseName = "NoticeConnect"

object SerializeConfig {

    fun main(path: Path) {

        val message = Message(listOf(firstMessage), listOf(joinMessage), listOf(leftMessage))
        val database = Database(databaseName)
        val replace = HashMap<String, String>()
        val discord = Discord("", DiscordMessage(discordFirstJoin, discordJoin, discordLeft))
        val config = Config(ver, message, database, replace, discord, defaultMode)

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
            if (verNode?.string != ver) {
                path.toFile().deleteOnExit()
                val fw = PrintWriter(BufferedWriter(OutputStreamWriter(FileOutputStream(path.toFile()), "UTF-8")))
                fw.write(string)
                fw.close()
            }
        }
    }

}

