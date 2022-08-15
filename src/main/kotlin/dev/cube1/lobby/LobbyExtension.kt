package dev.cube1.lobby

import dev.cube1.lobby.command.*
import dev.cube1.lobby.listener.Listener
import dev.cube1.lobby.task.ParticleTask
import dev.cube1.lobby.task.TabList
import dev.cube1.lobby.task.TimeSyncTask
import kotlinx.coroutines.DelicateCoroutinesApi
import net.minestom.server.MinecraftServer
import net.minestom.server.extensions.Extension
import net.kyori.adventure.key.Key;
import net.minestom.server.utils.NamespaceID
import java.time.Instant
import java.util.function.Supplier

class LobbyExtension : Extension() {
    override fun initialize(): LoadStatus {
        MinecraftServer.getBlockManager().registerHandler(NamespaceID.from(Key.key("minecraft:skull")), Supplier { SkullHandler() })
        MinecraftServer.getBlockManager().registerHandler(NamespaceID.from(Key.key("minecraft:sign")), Supplier { SignHandler() })
        Listener.run(eventNode())
        ParticleTask.run()
        TabList.run()
        MinecraftServer.getCommandManager().apply {
            register(GcCommand)
            register(FlyCommand)
            register(SitCommand)
            register(DateCommand)
            register(SpawnCommand)
            register(JukeboxCommand)
        }
        TimeSyncTask.updateTimeAdapter(Instant.now())
        val thread = Thread(TimeSyncTask)
        thread.start()

        logger().info("[LobbyExtension] has been enabled!")

        return LoadStatus.SUCCESS
    }

    override fun terminate() {
        Listener.armourStandSeatMap.clear()
        logger().info("[LobbyExtension] has been disabled!")
    }

}
