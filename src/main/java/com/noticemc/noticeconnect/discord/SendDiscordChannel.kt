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
package com.noticemc.noticeconnect.discord

import com.noticemc.noticeconnect.files.CustomConfig.Companion.config
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag
import javax.security.auth.login.LoginException

class SendDiscordChannel {
    var jda: JDA? = null

    init {
        val token: String? = config.node("discord", "token").string
        val channelID: String = config.node("discord", "channel").string.toString()
        try {
            jda = JDABuilder.createDefault(token, GatewayIntent.GUILD_MESSAGES).disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOTE).build()
            jda!!.awaitReady()
        } catch (e: LoginException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        textChannel = jda!!.getTextChannelById(channelID)
    }

    companion object {
        var textChannel: TextChannel? = null
    }
}
