package listeners

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
        if (entity.hasMetadata(PlayerEntityActionsListener.METADATA_OWNER_KEY)) {
            val ownerUUIDCandidates = entity.getMetadata(PlayerEntityActionsListener.METADATA_OWNER_KEY)
            val ownerUUIDMetadataValue = ownerUUIDCandidates.find { it.owningPlugin == plugin }
            val owner = entity.server.getPlayer(ownerUUIDMetadataValue?.value().toString())
            if (owner != null) {
                owner.sendMessage("${entity.name} has died!")
                if (owner.hasMetadata(PlayerEntityActionsListener.METADATA_PLAYER_FOLLOWER)) {
                    owner.removeMetadata(PlayerEntityActionsListener.METADATA_PLAYER_FOLLOWER, plugin)
                } else {
                    server.logger.warning("WARNING! Weird state, " +
                            "no metadata key `${PlayerEntityActionsListener.METADATA_PLAYER_FOLLOWER}` " +
                            "was found for player ${owner.uniqueId} when ${entity.uniqueId} was still registered " +
                            "as their follower")
                }
            }

        }
    }

}