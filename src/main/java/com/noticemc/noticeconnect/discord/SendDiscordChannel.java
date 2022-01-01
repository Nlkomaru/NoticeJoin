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

package com.noticemc.noticeconnect.discord;

import com.noticemc.noticeconnect.files.CustomConfig;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.EnumSet;
import java.util.Objects;

import javax.security.auth.login.LoginException;

public class SendDiscordChannel {

    static TextChannel channel;
    JDA jda;

    {
        String token = CustomConfig.getConfig().node("discord", "token").getString();
        String channelID = CustomConfig.getConfig().node("discord", "channel-id").getString();
        EnumSet<GatewayIntent> intent = EnumSet.of(GatewayIntent.GUILD_MESSAGES);

        try {
            jda = JDABuilder.createDefault(token, intent).disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOTE).build();
            jda.awaitReady();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
        channel = jda.getTextChannelById(Objects.requireNonNull(channelID));
    }

    public static TextChannel getTextChannel() {
        return channel;
    }


}