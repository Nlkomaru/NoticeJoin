package com.noticemc.noticeconnect.utils.command

import com.noticemc.noticeconnect.NoticeConnect
import com.velocitypowered.api.proxy.Player
import revxrsal.commands.command.CommandActor
import revxrsal.commands.command.ExecutableCommand
import revxrsal.commands.process.ValueResolver
import revxrsal.commands.velocity.VelocityCommandHandler

object  PlayerServerParser : ValueParser<Player>() {
    override fun suggestions(args: List<String>, sender: CommandActor, command: ExecutableCommand): Set<String> {
        return NoticeConnect.proxy.allPlayers.map { it.username }.toSet()
    }

    override fun resolve(context: ValueResolver.ValueResolverContext): Player {
        val playerName = context.pop()
        return NoticeConnect.proxy.getPlayer(playerName).orElseThrow { IllegalArgumentException("Player $playerName not found") }
    }

    fun VelocityCommandHandler.registerPlayerParser() {
        val handler = this
        handler.autoCompleter.registerParameterSuggestions(
            Player::class.java,
        ) { args: List<String>, sender: CommandActor, command: ExecutableCommand ->
            suggestions(args, sender, command)
        }
        handler.registerValueResolver(Player::class.java, this@PlayerServerParser)
    }
}