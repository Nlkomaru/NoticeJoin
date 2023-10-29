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