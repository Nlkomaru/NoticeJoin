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

import com.typesafe.config.ConfigFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.hocon.decodeFromConfig
import java.io.File
import java.nio.file.Path

class CustomConfig {
    @OptIn(ExperimentalSerializationApi::class)
    fun getConfigFile(dataDirectory: Path?) {
        val dataDirFile = dataDirectory!!.toFile()
        val file = File(dataDirFile, "config.conf")
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }


        SerializeConfig.main(file.toPath())

        config = Hocon.decodeFromConfig(ConfigFactory.parseFile(file))

    }

    companion object {
        lateinit var config: Config
    }
}