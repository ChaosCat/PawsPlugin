package core.metadata

import java.util.UUID

class SimpleUUIDListSerializer {
    /**
     * A very simple utility class for serializing UUID lists to be stored
     * as strings in persistentDataContainer / Metadata etc...
     *
     * This is much cheaper than using a serialization library
     */

    companion object {
        fun uuidMutListToString(list: MutableList<UUID>): String {
            var serialized = ""
            list.forEach { serialized += "$it," }
            return serialized.removeSuffix(",")
        }

        fun stringToMutUUIDList(string: String): MutableList<UUID> {
            return string.split(',').map {
                // This should never fail, throws IllegalArgumentException if there
                // is an unserializable UUID
                UUID.fromString(it)
            }.toMutableList()
        }
    }
}