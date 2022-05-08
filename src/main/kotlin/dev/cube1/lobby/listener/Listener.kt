package dev.cube1.lobby.listener

import com.google.common.io.ByteStreams
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.GameMode
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.event.player.PlayerMoveEvent
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.instance.AnvilLoader
import net.minestom.server.instance.Instance
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.utils.NamespaceID
import net.minestom.server.world.DimensionType
import net.minestom.server.world.DimensionType.DimensionTypeBuilder

object Listener {

    val spawn = Pos(0.5, 227.0, 0.5, -90.0F, 0F)
    val servers = hashMapOf(
        "wild" to Triple(22.5..28.5, 222.5..260.5, -2.5..3.5),
        "arcade" to Triple(-2.5..3.5, 222.5..260.5, -27.5..-21.5)
    )

    lateinit var instance: InstanceContainer

    fun run(eventNode: EventNode<Event>) {
        val dim = DimensionType.builder(NamespaceID.from("fullbright"))
            .ambientLight(10000.0F)
            .height(416)
            .logicalHeight(200)
            .minY(-64)
            .build()
        MinecraftServer.getDimensionTypeManager().addDimension(dim)
        instance = MinecraftServer.getInstanceManager().createInstanceContainer(dim)
        instance.chunkLoader = AnvilLoader("lobby")
        eventNode.addListener(PlayerMoveEvent::class.java) { event ->
            if(event.newPosition.y <= 180.0) {
                event.newPosition = spawn
            }
            servers.entries.find { entry ->
                entry.value.first.contains(event.newPosition.x) &&
                        entry.value.second.contains(event.newPosition.y) &&
                        entry.value.third.contains(event.newPosition.z)
            }?.key?.also { server ->
                val out = ByteStreams.newDataOutput()
                out.writeUTF("Connect")
                out.writeUTF(server)

                event.player.sendPluginMessage("BungeeCord", out.toByteArray())
            }
        }
        eventNode.addListener(PlayerLoginEvent::class.java) { event ->
            event.setSpawningInstance(instance)
        }
        eventNode.addListener(PlayerSpawnEvent::class.java) { event ->
            event.player.gameMode = GameMode.ADVENTURE
            event.player.teleport(spawn)
        }
    }

}