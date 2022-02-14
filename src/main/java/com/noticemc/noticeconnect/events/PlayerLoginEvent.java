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

package com.noticemc.noticeconnect.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public class PlayerLoginEvent {
    static HashSet<UUID> list  = new HashSet<>();
    @Subscribe public void onPlayerLogin(PlayerChooseInitialServerEvent event) {
        var player = event.getPlayer();
        if(player.hasPermission("noticeconnect.hide.join")) return;
        list.add(player.getUniqueId());
    }
}
