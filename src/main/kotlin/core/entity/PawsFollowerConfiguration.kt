package core.entity

import com.destroystokyo.paper.entity.ai.Goal
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Mob
import org.bukkit.entity.Player

data class PawsFollowerConfiguration<T : Mob?>(
    val followerType: EntityType,
) {
    val entitiesToAvoid: MutableList<EntityType> = mutableListOf()
    val entitiesToProtect: MutableList<EntityType> = mutableListOf()
    val tamingMaterial: Material = Material.AIR
    val removeGoals: MutableList<Goal<T>> = mutableListOf()
    val extraGoals: MutableList<Goal<T>> = mutableListOf()
    var owner: Player? = null
}