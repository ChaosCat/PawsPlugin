package routines

import listeners.PlayerEntityActionsListener
import org.bukkit.Server
import org.bukkit.entity.Mob
import org.bukkit.plugin.Plugin
import java.util.*

class FollowerPathfindRoutine(private val plugin: Plugin, private val server: Server) : Runnable {

    companion object {
        // Random delay in minecraft ticks
        val EXECUTION_DELAY: Long = 0
        // Execution interval in minecraft ticks
        val EXECUTION_INTERVAL: Long = 20
    }

    override fun run() {
        // TODO: This is a computationally intensive task and it runs periodically
        // TODO: May be better to find a caching mechanism
        val players = server.onlinePlayers
        for (player in players) {
            // This must be null-check exhaustive as these operations are not atomic and there may be
            // edge-case races
            if (player?.hasMetadata(PlayerEntityActionsListener.METADATA_PLAYER_FOLLOWER) == true) {
                val followerUUIDs = player?.getMetadata(PlayerEntityActionsListener.METADATA_PLAYER_FOLLOWER)
                val entityUUIDMetadataValue = followerUUIDs.find { it.owningPlugin == plugin } ?: continue
                val entityUUID = entityUUIDMetadataValue.value().toString()
                // server.logger.info("Entity UUID string: ${entityUUID}.")
                val entity = server.getEntity(UUID.fromString(entityUUID))
                if (entity is Mob) {
                    val follower = entity as? Mob
                    // Only re-route when no current target is set
                    if (follower?.target == null || follower?.target?.isDead == true) {
                        follower?.target = null
                        if (follower?.pathfinder?.hasPath() == true) {
                            follower?.pathfinder?.stopPathfinding()
                        }
                        val destination = player?.location
                        follower?.pathfinder?.moveTo(destination.add(1.0, 0.0, 1.0))
                    }
                }
            }
        }
    }

}