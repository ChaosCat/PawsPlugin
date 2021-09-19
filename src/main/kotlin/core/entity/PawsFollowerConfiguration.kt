package core.entity

import com.destroystokyo.paper.entity.ai.Goal
import com.destroystokyo.paper.entity.ai.GoalKey
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Mob
import org.bukkit.entity.Player

data class PawsFollowerConfiguration<T : Mob?>(
    val followerType: EntityType,
) {
    var entitiesToAvoid: MutableList<EntityType> = mutableListOf()
    var entitiesToProtect: MutableList<EntityType> = mutableListOf()
    var tamingMaterial: Material = Material.AIR
    var removeGoalKeys: MutableList<GoalKey<T>> = mutableListOf()
    var extraGoals: MutableList<Goal<T>> = mutableListOf()
    var owner: Player? = null
}