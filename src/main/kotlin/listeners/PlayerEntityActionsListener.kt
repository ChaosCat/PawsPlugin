package listeners

import com.destroystokyo.paper.entity.ai.GoalKey
import com.destroystokyo.paper.entity.ai.VanillaGoal
import core.entity.PawsFollowerConfiguration
import core.entity.PawsFollowerEntity
import core.entity.SupportedFollowerTypes
import org.bukkit.Material
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.plugin.Plugin
import java.util.*

class PlayerEntityActionsListener(val plugin: Plugin) : Listener {

    companion object {
        val METADATA_OWNER_KEY: String = "owner"
        // TODO: Handle multiple followers
        val METADATA_PLAYER_FOLLOWER: String = "follower"
    }

    @EventHandler
    fun onInteractEntity(event: PlayerInteractEntityEvent) {
        val entity = event.rightClicked
        if (entity.type in (SupportedFollowerTypes.values().map { it.entityType })) {
            val conf = PawsFollowerConfiguration<Mob>(entity.type)
            conf.owner = event.player
            conf.tamingMaterial = Material.GOLDEN_APPLE
            // TODO: Make custom goal presets for removal per - creature
            conf.removeGoalKeys.add(VanillaGoal.HURT_BY as GoalKey<Mob>)
            conf.removeGoalKeys.add(VanillaGoal.NEAREST_ATTACKABLE)
            // conf.removeGoalKeys.add(VanillaGoal.SILVERFISH_WAKE_UP_FRIENDS as GoalKey<Mob>)
            // econf.removeGoalKeys.add(VanillaGoal.SILVERFISH_MERGE_WITH_STONE as GoalKey<Mob>)
            val pawsEntity = PawsFollowerEntity(plugin, entity as Mob, conf)
            // TODO: An InteractionResult enum should be added
            // TODO: for filtering cases and applying more logic if necessary
            pawsEntity.onPlayerInteract(event)
        }
    }

    @EventHandler
    fun onPlayerAttack(entityDamageByEntityEvent: EntityDamageByEntityEvent) {
        // TODO: Add configuration logic and implement
        val damager = entityDamageByEntityEvent.damager
        val damagee = entityDamageByEntityEvent.entity
        val server = damagee.server
        if (damager.type == EntityType.PLAYER) {
            val player = entityDamageByEntityEvent.damager as Player
            val followerUUIDCandidates = player.getMetadata(METADATA_PLAYER_FOLLOWER)
            val followerUUIDMetadataValue =  followerUUIDCandidates.find { it.owningPlugin == plugin }
            followerUUIDMetadataValue?.value().let {
                damagee.server.logger.info("Setting target for ${it.toString()}")
                val follower = server.getEntity(UUID.fromString(it.toString()))
                if (follower is LivingEntity) {
                    follower.attack(damagee)
                }
                if (follower is Mob) {
                    follower.target = damagee as? LivingEntity
                }
            }
        }
    }
}