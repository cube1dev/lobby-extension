package dev.cube1.lobby.listener

import com.google.common.io.ByteStreams
import dev.cube1.lobby.util.showFireworkWithDuration
import dev.cube1.lobby.util.toMini
import net.minestom.server.MinecraftServer
import net.minestom.server.color.Color
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
import net.minestom.server.item.firework.FireworkEffect
import net.minestom.server.item.firework.FireworkEffectType
import net.minestom.server.utils.NamespaceID
import net.minestom.server.world.DimensionType
import net.minestom.server.world.DimensionType.DimensionTypeBuilder
import java.awt.Color.HSBtoRGB
import java.util.Random

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

        val messages = listOf(
            "{user}님이 두둥등장!",
            "{user}님이 접속하셨어요!",
            "{user}님, 안녕하세요!"
        )

        eventNode.addListener(PlayerSpawnEvent::class.java) { event ->
            event.player.gameMode = GameMode.ADVENTURE
            event.player.teleport(spawn)
            event.spawnInstance.players.forEach { player ->
                player.sendMessage("<bold><blue>CUBE <reset>${messages.random().replace(
                    "{user}", "<gold>${event.player.username}<reset>"
                )}".toMini())
            }
            val random = Random()
            val effects = mutableListOf(
                FireworkEffect(
                    false,//random.nextBoolean(),
                    false,//random.nextBoolean(),
                    FireworkEffectType.values().random(),
                    listOf(Color(random.nextInt(255), 255, 255)),
                    listOf(Color(random.nextInt(255), 255, 255))
                )
            )
            listOf(event.player).showFireworkWithDuration(instance, spawn, 20 * 3, effects)
        }
    }

}