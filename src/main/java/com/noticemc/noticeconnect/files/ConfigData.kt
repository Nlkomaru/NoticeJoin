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

package com.noticemc.noticeconnect.files

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.hocon.Hocon

@OptIn(ExperimentalSerializationApi::class)
val hocon = Hocon

@Serializable
data class DiscordMessage(val firstJoin: String, val join: String, val left: String)

@Serializable
data class Discord(val url: String, val message: DiscordMessage)

@Serializable
data class Message(val firstJoin: List<String>, val join: List<String>, val left: List<String>)

@Serializable
data class Database(val database: String)

@Serializable
data class Config(val version: String,
    val message: Message,
    val database: Database,
    val replace: Map<String, String>,
    val discord: Discord,
    val mode: String)