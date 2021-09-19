package core.commands

import org.bukkit.entity.LivingEntity

data class PawsFollowerCommandAttackTarget(val targetEntity: LivingEntity?) :
    PawsFollowerCommand(PawsFollowerCommandType.ATTACK_TARGET)
