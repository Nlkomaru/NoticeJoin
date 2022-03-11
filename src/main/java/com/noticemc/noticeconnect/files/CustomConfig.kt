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

import org.spongepowered.configurate.CommentedConfigurationNode
import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import org.spongepowered.configurate.loader.ConfigurationLoader
import java.io.File
import java.io.IOException
import java.nio.file.Path

class CustomConfig {
    fun getConfigFile(dataDirectory: Path?) {
        val dataDirFile = dataDirectory!!.toFile()
        val file = File(dataDirFile, "config.conf")
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }

        try {
            ObjectMapperExample.main(file.toPath())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val configManager: ConfigurationLoader<CommentedConfigurationNode> = HoconConfigurationLoader.builder().path(file.toPath()).build()
        try {
            config = configManager.load()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        lateinit var config: CommentedConfigurationNode
    }
}