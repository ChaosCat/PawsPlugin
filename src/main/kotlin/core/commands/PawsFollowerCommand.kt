package core.commands

sealed class PawsFollowerCommand(commandType: PawsFollowerCommandType) {
    val type = commandType
}