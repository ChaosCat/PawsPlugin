package core.metadata

import org.bukkit.NamespacedKey
import org.bukkit.entity.Entity
import org.bukkit.entity.Mob
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin
import java.util.*
//import kotlinx.serialization

class PawsMetadata {
    /**
     * This class bundles a collection of Paws-specific Metadata
     * helper functions and static metadata functions
     */
    companion object {
       enum class PawsMetadataKeys(val key: String) {
           FOLLOWERS("followers"),
           OWNER("owner")
       }

        fun hasFollowers(plugin: Plugin, entity: Entity) : Boolean {
            return entity.persistentDataContainer.has(NamespacedKey(plugin, PawsMetadataKeys.FOLLOWERS.key),
                PersistentDataType.STRING)
        }

        fun getFollowerUUIDsOf(plugin: Plugin, player: Player) : MutableList<UUID>? {
            if (!hasFollowers(plugin, player)) {
                return null
            }
            val followerListString = player.persistentDataContainer.get(
            NamespacedKey(plugin, PawsMetadataKeys.FOLLOWERS.key), PersistentDataType.STRING
            )
            return followerListString?.let { SimpleUUIDListSerializer.stringToMutUUIDList(it) }
        }

        fun isFollowerOf(plugin: Plugin, player: Player, entity: Entity) : Boolean {
            if (!hasFollowers(plugin, player)) {
                return false
            }
            return getFollowerUUIDsOf(plugin, player)?.contains(entity.uniqueId) ?: false
        }

        fun addFollowerOf(plugin: Plugin, player: Player, follower: Entity) : Boolean {
            if (isFollowerOf(plugin, player, follower)) {
                return false
            }
            var followers = getFollowerUUIDsOf(plugin, player)

            if (followers == null) {
                followers = mutableListOf(follower.uniqueId)
            } else {
                followers.add(follower.uniqueId)
            }

            plugin.server.logger.info("DEBUG New UUID list: " +
                    SimpleUUIDListSerializer.uuidMutListToString(followers)
            )
            player.persistentDataContainer.set(
                NamespacedKey(plugin, PawsMetadataKeys.FOLLOWERS.key),
                PersistentDataType.STRING,
                SimpleUUIDListSerializer.uuidMutListToString(followers)
            )
            return true
        }

        fun hasOwner(plugin: Plugin, entity: Entity) : Boolean {
            return entity.persistentDataContainer.has(NamespacedKey(plugin, PawsMetadataKeys.OWNER.key),
                PersistentDataType.STRING)
        }

        fun getOwner(plugin: Plugin, entity: Entity) : Player? {
            if (!hasOwner(plugin, entity)) {
                return null
            }
            return plugin.server.getPlayer(UUID.fromString(
                entity.persistentDataContainer.get(NamespacedKey(plugin, PawsMetadataKeys.OWNER.key),
                    PersistentDataType.STRING)
            ))
        }

        fun isOwner(plugin: Plugin, entity: Entity, player: Player) : Boolean {
            return getOwner(plugin, entity) == player
        }

        fun getEntityUUIDString(entity: Entity) : String {
            return entity.uniqueId.toString()
        }

        fun registerOwner(plugin: Plugin, entity: Entity, player: Player) : Boolean {
            if (hasOwner(plugin, entity)) {
                return false
            }
            val playerUUID = getEntityUUIDString(player)
            entity.persistentDataContainer.set(NamespacedKey(plugin,
                PawsMetadataKeys.OWNER.key), PersistentDataType.STRING,
                playerUUID
            )
            return true
        }

        fun removeEntityOwnerKey(plugin: Plugin, entity: Entity) {
            entity.persistentDataContainer.remove(NamespacedKey(plugin,
            PawsMetadataKeys.OWNER.key))
        }

        fun removePlayerFollowerKey(plugin: Plugin, player: Player) {
            player.persistentDataContainer.remove(
                NamespacedKey(plugin, PawsMetadataKeys.FOLLOWERS.key)
            )
        }

        fun getFollowersOf(plugin: Plugin, player: Player?): MutableList<Mob> {
            val mobList = mutableListOf<Mob>()
            var entity: Entity? = null
            for (uuid in player?.let { getFollowerUUIDsOf(plugin, it) }!!) {
                entity = plugin.server.getEntity(uuid)
                if (entity != null && entity is Mob) {
                    mobList.add(entity)
                }
            }
            return mobList
        }

    }
}
