package core.entity

import com.destroystokyo.paper.ParticleBuilder
import com.destroystokyo.paper.entity.ai.VanillaGoal
import core.commands.PawsFollowerCommand
import core.commands.PawsFollowerCommandAttackTarget
import core.commands.PawsFollowerCommandType
import listeners.PlayerEntityActionsListener
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.EntityType
import org.bukkit.entity.Mob
import org.bukkit.entity.Silverfish
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.Plugin

class PawsFollowerEntity(val plugin: Plugin, val mobEntity: Mob, override val configuration: PawsFollowerConfiguration<Mob>) :
    AbstractPawsFollowerEntity<Mob> {
    override fun onMobTame() {
        // Reset current aggression
        mobEntity.target = null
        for (goal in configuration.removeGoals) {
            Bukkit.getMobGoals().removeGoal(mobEntity, goal)
        }
    }

    override fun onPlayerAttackEnemy(entityDamageByEntityEvent: EntityDamageByEntityEvent) {
        // Extra checks to validate it's indeed the owner
    }

    override fun areTamingConditionsMet(event: PlayerInteractEntityEvent): Boolean {
        val player = event.player
        val mainItem = player.inventory.itemInMainHand
        val offItem = player.inventory.itemInOffHand
        val targetEntity = event.rightClicked
        val server = event.player.server

        if (mainItem.type == configuration.tamingMaterial && event.hand == EquipmentSlot.HAND
            && targetEntity.type == configuration.followerType) {
            server.logger.info("Matched taming conditions for ${ targetEntity.name } " +
                    "with material ${mainItem.type}")
            return true
        }
        return false
    }

    override fun onPlayerInteract(event: PlayerInteractEntityEvent) {
        val player = event.player
        val mainItem = player.inventory.itemInMainHand
        val offItem = player.inventory.itemInOffHand
        val targetEntity = event.rightClicked
        val server = event.player.server

        if (targetEntity is Mob) {
            // DEBUG: Show goals
            val goals = Bukkit.getMobGoals().getAllGoals(targetEntity)
            for (goal in goals) {
                server.logger.info(goal.key.toString())
            }
        }

        if (areTamingConditionsMet(event)) {
            if (!targetEntity.hasMetadata(PlayerEntityActionsListener.METADATA_OWNER_KEY)) {
                server.logger.info("Player ${player.name} tamed ${targetEntity.name}")
                val silverfish = targetEntity as Silverfish

                val goals = Bukkit.getMobGoals()
                goals.removeGoal(silverfish, VanillaGoal.NEAREST_ATTACKABLE)
                goals.removeGoal(silverfish, VanillaGoal.HURT_BY)
                goals.removeGoal(silverfish, VanillaGoal.SILVERFISH_MERGE_WITH_STONE)
                goals.removeGoal(silverfish, VanillaGoal.SILVERFISH_WAKE_UP_FRIENDS)

                // Unfortunately it seems that the UUIDs must be stored serialized
                server.logger.info("Owner UUID: ${player.uniqueId}")
                val data = FixedMetadataValue(plugin, player.uniqueId.toString())
                silverfish.setMetadata(PlayerEntityActionsListener.METADATA_OWNER_KEY, data)

                val targetUUID = targetEntity.uniqueId.toString()
                player.setMetadata(
                    PlayerEntityActionsListener.METADATA_PLAYER_FOLLOWER,
                    FixedMetadataValue(plugin, targetUUID)
                )
            } else {
                val uuidCandidates = targetEntity.getMetadata(PlayerEntityActionsListener.METADATA_OWNER_KEY)
                val uuidMetadata = uuidCandidates.find { it.owningPlugin == plugin }
                if (uuidMetadata != null) {
                    if (uuidMetadata.value().toString() == player.uniqueId.toString()) {
                        // player.sendMessage("You already own this creature!")
                        val loveParticleBuilder = ParticleBuilder(Particle.HEART)
                        loveParticleBuilder.count(20)
                        loveParticleBuilder.location(targetEntity.location)
                        loveParticleBuilder.receivers(100)
                        loveParticleBuilder.spawn()
                    } else {
                        player.sendMessage("This creature is owned by another player!")
                    }
                }
            }
        }
    }

    override fun onPlayerAttacked(entityDamageByEntityEvent: EntityDamageByEntityEvent) {
    }

    override fun onOtherFollowerAttacked(entityDamageByEntityEvent: EntityDamageByEntityEvent) {
        TODO("Not yet implemented")
    }

    override fun command(command: PawsFollowerCommand) {
        when (command.type) {
            PawsFollowerCommandType.WAIT -> {
                mobEntity.pathfinder.stopPathfinding()
                // TODO: Find a normal way to WAIT / FOLLOW
                mobEntity.isAware = false
            }
            PawsFollowerCommandType.FOLLOW -> {
                mobEntity.isAware = true
            }
            PawsFollowerCommandType.ATTACK_TARGET -> {
                val attackTargetCommand = command as PawsFollowerCommandAttackTarget
                mobEntity.target = attackTargetCommand.targetEntity
            }
            PawsFollowerCommandType.UNKNOWN -> {
                mobEntity.server.logger.warning("Unknown command type invoked")
            }
            else -> {
                mobEntity.server.logger.warning("Unhandled/ unimplemented command invoked")
            }
        }
    }

    // Default behavior is just killing the mob and de-regiistering it
    override fun onMobRelease() {
        mobEntity.pathfinder.stopPathfinding()
        mobEntity.remove()
    }
}