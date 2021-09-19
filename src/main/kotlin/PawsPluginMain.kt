import listeners.EntityEventsListener
import listeners.PlayerEntityActionsListener
import routines.FollowerPathfindRoutine
import org.bukkit.plugin.java.JavaPlugin

class PawsPluginMain : JavaPlugin() {

    override fun onEnable() {
        registerListeners()
        startRoutines()
        logger.info("Paws Plugin Enabled!")
    }

    private fun registerListeners() {
        val entityEventsListener = EntityEventsListener(this)
        val playerEntityActionsListener = PlayerEntityActionsListener(this)
        server.pluginManager.registerEvents(entityEventsListener, this)
        server.pluginManager.registerEvents(playerEntityActionsListener, this)
    }

    private fun startRoutines() {
        server.scheduler.runTaskTimer(this,
            FollowerPathfindRoutine(this, server),
            FollowerPathfindRoutine.EXECUTION_DELAY,
            FollowerPathfindRoutine.EXECUTION_INTERVAL
        )
    }

    override fun onDisable() {
        logger.info("Paws Plugin Disabled")
    }
}