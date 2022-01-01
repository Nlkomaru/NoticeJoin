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

package com.noticemc.noticeconnect.commands;

import com.noticemc.noticeconnect.NoticeConnect;
import com.noticemc.noticeconnect.database.Database;
import com.noticemc.noticeconnect.discord.SendDiscordChannel;
import com.noticemc.noticeconnect.files.CustomConfig;
import com.velocitypowered.api.command.SimpleCommand;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.Objects;

public class ReloadCommand implements SimpleCommand {


    Database sql = null;

    private final Logger logger = NoticeConnect.getLogger();
    private final Path dir = NoticeConnect.getPath();



    @Override public void execute(Invocation invocation) {
        logger.info("Reloading...");
        CustomConfig config = new CustomConfig();
        config.getConfigFile(dir);
        sqlConnection();

        if (!(Objects.equals(CustomConfig.getConfig().node("discord", "token").getString(), "")
                || Objects.equals(CustomConfig.getConfig().node("discord", "channel-id").getString(), ""))) {
            new SendDiscordChannel();
        }
        logger.info("NoticeConnectを再読み込みしました");
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission("noticeconnect.reload");
    }

    private void sqlConnection() {
        sql = new Database();
        try {
            sql.connect(dir);
        } catch (Exception e) {
            logger.error("Failed to connect to database");
            e.printStackTrace();
        }
        if (Database.isConnected()) {
            logger.info("Connected to database");
        }
        Database.initializeDatabase();
    }
}
