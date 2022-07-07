/*
 * <NoticeConnect>-<A login message plugin that runs on Velocity>
 *
 * Written in 2022  by Nikomaru <nikomaru@nikomaru.dev>
 *
 *   To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.
 *   This software is distributed without any warranty.
 *
 *   You should have received a copy of the CC0 Public Domain Dedication along with this software.
 *   If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package com.noticemc.noticeconnect

import com.noticemc.noticeconnect.NoticeConnect.Companion.logger
import com.noticemc.noticeconnect.discord.DiscordWebhookData
import com.noticemc.noticeconnect.files.CustomConfig
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

object Utils {

    fun sendAudienceMessage(message: Component) {
        val audience: Audience = NoticeConnect.proxy

        if (CustomConfig.config.mode == "ActionBar") {
            audience.sendActionBar(message)
        } else {
            audience.sendMessage(message)
        }

    }

    val mm = MiniMessage.miniMessage()

    fun DiscordWebhookData.sendWebHook() {
        val json = Json.encodeToString(this)
        val url = CustomConfig.config.discord.url
        if (url.isBlank()) {
            return
        }

        try {
            val webHookUrl = URL(url)
            val con: HttpsURLConnection = (webHookUrl.openConnection() as HttpsURLConnection)

            con.addRequestProperty("Content-Type", "application/JSON; charset=utf-8")
            con.addRequestProperty("User-Agent", "DiscordBot")
            con.doOutput = true
            con.requestMethod = "POST"

            con.setRequestProperty("Content-Length", json.length.toString())

            val stream: OutputStream = con.outputStream
            stream.write(json.toByteArray(Charsets.UTF_8))
            stream.flush()
            stream.close()

            val status: Int = con.responseCode
            if (status != HttpURLConnection.HTTP_OK && status != HttpURLConnection.HTTP_NO_CONTENT) {
                logger.warn("error:$status")
            }
            con.disconnect()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}