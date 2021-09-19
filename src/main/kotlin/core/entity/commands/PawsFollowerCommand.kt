package core.entity.commands

sealed class PawsFollowerCommand(commandType: PawsFollowerCommandType) {
    val type = commandType
}