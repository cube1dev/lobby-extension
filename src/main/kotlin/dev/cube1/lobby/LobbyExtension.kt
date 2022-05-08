package dev.cube1.lobby

import net.minestom.server.extensions.Extension;

class LobbyExtension : Extension() {

    override fun initialize(): LoadStatus {
        logger().info("[LobbyExtension] has been enabled!")

        return LoadStatus.SUCCESS
    }

    override fun terminate() {
        logger().info("[LobbyExtension] has been disabled!")
    }

}
