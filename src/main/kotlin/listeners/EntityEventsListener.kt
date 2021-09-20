package listeners

import core.metadata.PawsMetadata
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.plugin.Plugin

class EntityEventsListener(private val plugin: Plugin) : Listener {

    @EventHandler
    fun onEntitySpawn(event: EntitySpawnEvent) {
        val entity = event.entity
        entity.isCustomNameVisible = true
    }

    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) {
        val entity = event.entity
        val server = entity.server
        if (PawsMetadata.hasOwner(plugin, entity)) {
            val owner = PawsMetadata.getOwner(plugin, entity)
            PawsMetadata.removeEntityOwnerKey(plugin, entity)
            owner?.let {
                it.sendMessage("${entity.name} has died!")
                PawsMetadata.removeFollowerOf(plugin, it, entity)
            }
        }
    }

}