/*
 * <NoticeJoin>-<A login message plugin that runs on Velocity>
 *
 * Written in 2021  by Nikomaru <nikomaru@nikomaru.dev>
 *
 *   To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.
 *   This software is distributed without any warranty.
 *
 *   You should have received a copy of the CC0 Public Domain Dedication along with this software.
 *   If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package com.noticemc.noticeconnect.files;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class CustomConfig {
    static CommentedConfigurationNode config;

    static public CommentedConfigurationNode getConfig() {
        return config;
    }

    public void getConfigFile(Path dataDirectory) {
        File dataDirFile = dataDirectory.toFile();
        File file = new File(dataDirFile, "config.conf");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (!file.exists()) {
            try {
                InputStream input = this.getClass().getClassLoader().getResourceAsStream("config.conf");
                if (input != null) {
                    Files.copy(input, file.toPath());
                } else {
                    file.createNewFile();
                }

                Objects.requireNonNull(input).close();
            } catch (IOException exception) {
                exception.printStackTrace();
                return;
            }
        }
        ConfigurationLoader<CommentedConfigurationNode> configManager = HoconConfigurationLoader.builder().setFile(file)
                .build();
        try {
            config = configManager.load();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
