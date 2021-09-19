package util

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.UUID

class MetadataUtils {
    fun UUID?.toPlayer(uuid: UUID?): Player? {
        if (uuid != null) {
            return Bukkit.getPlayer(uuid)
        }
        return null
    }

    fun getFollower(player: Player?) {

    }

}