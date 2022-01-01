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
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class ObjectMapperExample {

    public static void main(Path path) throws ConfigurateException {
        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .path(path) // or url(), or source/sink
                .build();

        final CommentedConfigurationNode node = loader.load(); // Load from file
        final Database database = new Database(); // Populate object
        final Message message = new Message();
        final Replace replace = new Replace();
        final Discord discord = new Discord();
        final DiscordMessage discordMessage = new DiscordMessage();

        // Do whatever actions with the configuration, then...
        node.node("message").set(message);
        node.node("database").set(database); // Update the backing node
        node.set(replace);
        node.node("discord").set(discord).node("message").set(discordMessage);
        // the backing node
        loader.save(node); // Write to the original file
    }


    @ConfigSerializable
    static class Database {

        private final String database = "NoticeConnect";

    }

    @ConfigSerializable
    static class Message {

        private final String firstJoin = "<gray>[<yellow>初見さんいらっしゃい</yellow>] <light_purple><name>さんがはじめて<currentServerName>サーバーに入りました!";

        private final String join = "<yellow><gold><name></gold>さんが<gold>サーバー<gold><gray>(<currentServerName>)</gray>にやってきました!";

        private final String left = "<yellow><gold><name></gold>さんが<gold>サーバー<gold><gray>(<currentServerName>)</gray>から離れました";

    }

    @ConfigSerializable
    static class Replace {

        @Comment("Example: test = \"テスト\" It needs to be written like this")
        private final Map<String, String> replace = new HashMap<>();


    }

    @ConfigSerializable
    static class Discord {

        private final String token = "";

        private final String channelId = "";

    }


    @ConfigSerializable
    static class DiscordMessage {

        private final String firstJoin = "%(PlayerName) さんがサーバーに初参加です";

        private final String join = "%(PlayerName) さんがサーバーに参加しました";

        private final String left = "%(PlayerName) さんがサーバーから退出しました";

    }
}