package dev.cube1.lobby

import dev.cube1.lobby.command.GcCommand
import dev.cube1.lobby.listener.Listener
import dev.cube1.lobby.task.ParticleTask
import net.minestom.server.MinecraftServer
import net.minestom.server.extensions.Extension;

class LobbyExtension : Extension() {

    override fun initialize(): LoadStatus {
        Listener.run(eventNode())
        ParticleTask.run()
        MinecraftServer.getCommandManager().register(GcCommand())
        logger().info("[LobbyExtension] has been enabled!")

        return LoadStatus.SUCCESS
    }

    override fun terminate() {
        logger().info("[LobbyExtension] has been disabled!")
    }

}
