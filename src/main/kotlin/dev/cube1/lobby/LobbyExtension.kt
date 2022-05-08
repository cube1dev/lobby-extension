package dev.cube1.lobby

import dev.cube1.lobby.listener.Listener
import dev.cube1.lobby.task.ParticleTask
import net.minestom.server.extensions.Extension;

class LobbyExtension : Extension() {

    override fun initialize(): LoadStatus {
        ParticleTask.run()
        Listener.run(eventNode())
        logger().info("[LobbyExtension] has been enabled!")

        return LoadStatus.SUCCESS
    }

    override fun terminate() {
        logger().info("[LobbyExtension] has been disabled!")
    }

}
