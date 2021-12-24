/*
 *  <NoticeConnect>-<A login message plugin that runs on Velocity>
 *
 *  Written in 2021  by Nikomaru <nikomaru@nikomaru.dev>
 *
 *      To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.
 *      This software is distributed without any warranty.
 *
 *      You should have received a copy of the CC0 Public Domain Dedication along with this software.
 *      If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package com.noticemc.noticeconnect.database;

import com.noticemc.noticeconnect.files.CustomConfig;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class Database {
    public static Connection connection;

    public static Connection getConnection() {
        return connection;
    }

    public static void initializeDatabase() {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(
                    "CREATE TABLE IF NOT EXISTS JoinedPlayerList (UUID VARCHAR(40) NOT NULL)");
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public boolean isConnected() {
        return connection != null;
    }

    public void connect() throws Exception {
        CommentedConfigurationNode databaseNode = CustomConfig.getConfig().getNode("database");
        if (!isConnected()) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + Objects.requireNonNull(databaseNode).getNode("host")
                            .getString() + ":" + databaseNode.getNode("port").getInt() + "/" + databaseNode.getNode(
                            "database").getString() + "?useSSL=false", databaseNode.getNode("user").getString(),
                    databaseNode.getNode("password").getString());
        }
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

