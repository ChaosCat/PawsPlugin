package executors

import core.metadata.PawsMetadata
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class CommandClearPlayerFollowersKey(private val plugin: Plugin) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val player = sender as Player
            PawsMetadata.removePlayerFollowerKey(plugin, player)
        }
        return true
    }
}