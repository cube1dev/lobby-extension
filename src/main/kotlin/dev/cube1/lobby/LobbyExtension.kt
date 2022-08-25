package dev.cube1.lobby

import dev.cube1.lobby.command.*
import dev.cube1.lobby.handler.CampfireHandler
import dev.cube1.lobby.handler.SignHandler
import dev.cube1.lobby.handler.SkullHandler
import dev.cube1.lobby.listener.Listener
import dev.cube1.lobby.task.ParticleTask
import dev.cube1.lobby.task.TabList
import dev.cube1.lobby.task.TimeSyncTask
import net.minestom.server.MinecraftServer
import net.minestom.server.extensions.Extension
import net.kyori.adventure.key.Key;
import net.minestom.server.utils.NamespaceID
import java.time.Instant
import java.util.function.Supplier

class LobbyExtension : Extension() {
    override fun initialize(): LoadStatus {
        MinecraftServer.getBlockManager().apply {
            registerHandler(NamespaceID.from(Key.key("minecraft:skull"))) { SkullHandler() }
            registerHandler(NamespaceID.from(Key.key("minecraft:sign"))) { SignHandler() }
            registerHandler(NamespaceID.from(Key.key("minecraft:campfire"))) { CampfireHandler() }
        }
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
