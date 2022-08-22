package dev.cube1.lobby.listener

import com.google.common.io.ByteStreams
import dev.cube1.lobby.util.createIndicator
import dev.cube1.lobby.util.showFireworkWithDuration
import dev.cube1.lobby.util.toMini
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import net.kyori.adventure.text.Component
import net.minestom.server.MinecraftServer
import net.minestom.server.color.Color
import net.minestom.server.coordinate.Point
import net.minestom.server.coordinate.Pos
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.Entity
import net.minestom.server.entity.EntityType
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import net.minestom.server.entity.metadata.other.ArmorStandMeta
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerBlockInteractEvent
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.event.player.PlayerMoveEvent
import net.minestom.server.event.player.PlayerPacketEvent
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.instance.AnvilLoader
import net.minestom.server.instance.Instance
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.instance.block.Block
import net.minestom.server.item.firework.FireworkEffect
import net.minestom.server.item.firework.FireworkEffectType
import net.minestom.server.network.packet.client.play.ClientSteerVehiclePacket
import net.minestom.server.resourcepack.ResourcePack
import net.minestom.server.utils.NamespaceID
import net.minestom.server.world.DimensionType
import net.minestom.server.world.DimensionType.DimensionTypeBuilder
import java.awt.Color.HSBtoRGB
import java.util.Random
import java.util.concurrent.ConcurrentHashMap

@Suppress("UnstableApiUsage")
object Listener {

    val armourStandSeatMap = ConcurrentHashMap<Entity, Point>()

    val spawn = Pos(0.5, 116.0, 0.5, -90.0F, 0F)
    val servers = hashMapOf(
        "race" to Triple(15.5..17.5, 111.5..114.5, -13.5..-11.5),
        "wild" to Triple(14.5..16.5, 112.5..115.5, 15.5..17.5),
        "pit" to Triple(13.5..15.5, 110.5..115.5, -2.5..-0.5)
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
        instance.enableAutoChunkLoad(true)

        instance.createIndicator("<bold><red>레<white>이<red>스".toMini(), Pos(16.5, 113.0, -12.5))
        instance.createIndicator("<bold><green>야생".toMini(), Pos(15.5, 114.0, 16.5))
        instance.createIndicator("<bold><yellow>핏 경기장".toMini(), Pos(14.5, 113.0, -1.5))

        eventNode.addListener(PlayerMoveEvent::class.java) { event ->
            if (event.newPosition.x >= 80 || event.newPosition.x <= -80 || event.newPosition.y <= 80 ||
                event.newPosition.z >= 80 || event.newPosition.z <= -80) {
                event.newPosition = spawn
            }
            servers.entries.find { entry ->
                entry.value.first.contains(event.newPosition.x) &&
                        entry.value.second.contains(event.newPosition.y) &&
                        entry.value.third.contains(event.newPosition.z)
            }?.key?.also { server ->
                event.player.sendPlayerListHeaderAndFooter(Component.empty(), Component.empty())
                val out = ByteStreams.newDataOutput()
                out.writeUTF("Connect")
                out.writeUTF(server)

                event.player.sendPluginMessage("BungeeCord", out.toByteArray())
            }
        }
        eventNode.addListener(PlayerLoginEvent::class.java) { event ->
            event.setSpawningInstance(instance)
        }
        eventNode.addListener(PlayerBlockInteractEvent::class.java) { event ->
            event.run {
                // stealed from emortalmc/lobbyextension
                if (block.compare(Block.SPRUCE_STAIRS, Block.Comparator.ID) || block == Block.WHITE_CARPET) {
                    if (player.vehicle != null) return@addListener
                    if (armourStandSeatMap.values.contains(blockPosition)) return@addListener
                    if (block.getProperty("half") == "top") return@addListener

                    val armourStand = Entity(EntityType.ARMOR_STAND)
                    val armourStandMeta = armourStand.entityMeta as ArmorStandMeta
                    armourStandMeta.setNotifyAboutChanges(false)
                    armourStandMeta.isSmall = true
                    armourStandMeta.isHasNoBasePlate = true
                    armourStandMeta.isMarker = true
                    armourStandMeta.isInvisible = true
                    armourStandMeta.setNotifyAboutChanges(true)
                    armourStand.setNoGravity(true)

                    val spawnPos = blockPosition.add(0.5, 0.3, 0.5)
                    val yaw = when (block.getProperty("facing")) {
                        "east" -> 90f
                        "south" -> 180f
                        "west" -> -90f
                        else -> 0f
                    }

                    armourStand.setInstance(instance, Pos(spawnPos, yaw, 0f))
                        .thenRun {
                            armourStand.addPassenger(player)
                        }

                    armourStandSeatMap[armourStand] = blockPosition
                }
            }
        }
        eventNode.addListener(PlayerPacketEvent::class.java) { event ->
            event.run {
                if(packet is ClientSteerVehiclePacket) {
                    val steerPacket = packet as ClientSteerVehiclePacket
                    if (steerPacket.flags.toInt() == 2) {
                        if (player.vehicle != null && player.vehicle !is Player) {
                            val entity = player.vehicle!!
                            entity.removePassenger(player)

                            if (armourStandSeatMap.containsKey(entity)) {
                                armourStandSeatMap.remove(entity)
                                entity.remove()
                                player.velocity = Vec(0.0, 10.0, 0.0)
                            }
                        }
                        return@addListener
                    }
                }
            }
        }

        val messages = listOf(
            "{user}님이 두둥등장!",
            "{user}님이 접속하셨어요!",
            "{user}님, 안녕하세요!",
            "{user}(이)가 서버에 들어오기 아이템을(를) 사용했다!"
        )

        eventNode.addListener(PlayerSpawnEvent::class.java) { event ->
            event.player.setResourcePack(ResourcePack.optional("https://static.planetminecraft.com/files/resource_media/texture/easyblocks-e3380.zip", null))
            event.player.gameMode = GameMode.ADVENTURE
            event.player.teleport(spawn)
            val msg = "<bold><aqua>PROJECT_TL'S PRIVATE SERVER <reset>${messages.random().replace(
                "{user}", "<bold>${event.player.username}<reset>"
            )}".toMini()
            event.spawnInstance.players.forEach { player ->
                player.sendMessage(msg)
            }
            val random = Random()
            val effects = mutableListOf(
                FireworkEffect(
                    false, //random.nextBoolean(),
                    false, //random.nextBoolean(),
                    FireworkEffectType.LARGE_BALL,
                    listOf(Color(random.nextInt(255), 255, 255)),
                    listOf(Color(random.nextInt(255), 255, 255))
                )
            )
            event.spawnInstance.players.showFireworkWithDuration(instance, spawn, effects)
        }
    }
}