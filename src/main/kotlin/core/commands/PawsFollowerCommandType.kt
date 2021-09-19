package core.commands

import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType

enum class PawsFollowerCommandType {
    WAIT,
    FOLLOW,
    ATTACK_TARGET,
    PROTECT_TARGET,
    ATTACK_ENTITY_TYPE,
    PROTECT_ENTITY_TYPE,
    PATROL_AREA_CIRCLE,
    PATROL_AREA_ROUTE,
    UNKNOWN
}