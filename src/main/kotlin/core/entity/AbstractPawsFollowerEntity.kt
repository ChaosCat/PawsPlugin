package core.entity

import core.commands.PawsFollowerCommand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Mob
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEntityEvent

interface AbstractPawsFollowerEntity<MobType: Mob?> {
    /**
     * Base PawsFollowerEntity interface, this is the class from which
     * more specialized mobs with commands should extend, PawsFollowerEntity
     * base implementation class will contain the basic expected behavior and rules
     * which can be overriden / specialized
     */
    val configuration: PawsFollowerConfiguration<MobType>

    fun onMobTame()
    fun onMobRelease()
    fun onPlayerAttackEnemy(entityDamageByEntityEvent: EntityDamageByEntityEvent)
    fun onPlayerInteract(event: PlayerInteractEntityEvent)
    fun onPlayerAttacked(entityDamageByEntityEvent: EntityDamageByEntityEvent)
    fun onOtherFollowerAttacked(entityDamageByEntityEvent: EntityDamageByEntityEvent)
    fun areTamingConditionsMet(event: PlayerInteractEntityEvent): Boolean
    fun command(command: PawsFollowerCommand)
}