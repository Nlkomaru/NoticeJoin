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

package com.noticemc.noticeconnect.files;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

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
                ObjectMapperExample.main(file.toPath());
            } catch (ConfigurateException e) {
                e.printStackTrace();
            }
        }
        ConfigurationLoader<CommentedConfigurationNode> configManager = HoconConfigurationLoader.builder()
                .path(file.toPath()).build();
        try {
            config = configManager.load();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
