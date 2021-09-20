package core.entity

import org.bukkit.entity.EntityType

enum class SupportedFollowerTypes(val entityType: EntityType) {
    /**
     * This class acts as a basic filter for avoiding bloating every interaction
     * and restricting to attempt taming logic applicane only on supported Enitities
     */
    SILVERFISH(EntityType.SILVERFISH),
    ZOMBIE(EntityType.ZOMBIE),
    SKELETON(EntityType.SKELETON),
    CREEPER(EntityType.CREEPER),
    SPIDER(EntityType.SPIDER),
    SNOWMAN(EntityType.SNOWMAN)
}