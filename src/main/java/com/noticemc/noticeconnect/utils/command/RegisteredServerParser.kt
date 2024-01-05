/*
 * NoticeConnect-A login message plugin that runs on Velocity
 *
 * Written in 2021-2024  by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.
 * This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package com.noticemc.noticeconnect.utils.command

import com.noticemc.noticeconnect.NoticeConnect
import com.velocitypowered.api.proxy.server.RegisteredServer
import revxrsal.commands.command.CommandActor
import revxrsal.commands.command.ExecutableCommand
import revxrsal.commands.process.ValueResolver
import revxrsal.commands.velocity.VelocityCommandHandler

object  RegisteredServerParser : ValueParser<RegisteredServer>() {
    override fun suggestions(args: List<String>, sender: CommandActor, command: ExecutableCommand): Set<String> {
        return NoticeConnect.proxy.allServers.map { it.serverInfo.name }.toSet()
    }

    override fun resolve(context: ValueResolver.ValueResolverContext): RegisteredServer {
        val serverName = context.pop()
        return NoticeConnect.proxy.getServer(serverName).orElseThrow { IllegalArgumentException("Server $serverName not found") }
    }

    fun VelocityCommandHandler.registerServerParser() {
        val handler = this
        handler.autoCompleter.registerParameterSuggestions(
            RegisteredServer::class.java,
        ) { args: List<String>, sender: CommandActor, command: ExecutableCommand ->
            suggestions(args, sender, command)
        }
        handler.registerValueResolver(RegisteredServer::class.java, this@RegisteredServerParser)
    }

}