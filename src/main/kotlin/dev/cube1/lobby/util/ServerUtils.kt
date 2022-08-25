package dev.cube1.lobby.util

import com.google.common.io.ByteStreams
import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player

@Suppress("UnstableApiUsage")
fun Player.moveServer(server: String) {
    this.sendPlayerListHeaderAndFooter(Component.empty(), Component.empty())
    val out = ByteStreams.newDataOutput()
    out.writeUTF("Connect")
    out.writeUTF(server)

    this.sendPluginMessage("BungeeCord", out.toByteArray())
}