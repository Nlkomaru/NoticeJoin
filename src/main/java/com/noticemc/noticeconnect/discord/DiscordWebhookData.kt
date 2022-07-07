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

package com.noticemc.noticeconnect.discord

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiscordWebhookData(val embeds: ArrayList<DiscordWebhookEmbed>)

@Serializable
data class DiscordWebhookEmbed(var color: Int, var author: Author)

@Serializable
data class Author(val name: String, val url: String? = null, @SerialName("icon_url") val iconUrl: String)