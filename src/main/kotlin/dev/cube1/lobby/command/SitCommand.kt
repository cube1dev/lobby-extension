package dev.cube1.lobby.command

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.builder.Command
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Entity
import net.minestom.server.entity.EntityType
import net.minestom.server.entity.Player
import net.minestom.server.entity.metadata.other.ArmorStandMeta
import net.minestom.server.instance.block.Block

// stealed from emortalmc/lobbyextension
object SitCommand: Command("sit") {
    init {
        setDefaultExecutor { sender, _ ->
            if (sender !is Player) {
                sender.sendMessage(Component.text("This command is player only", NamedTextColor.RED))
                return@setDefaultExecutor
            }

            if (sender.vehicle != null) return@setDefaultExecutor

            var i = 0
            while (true) {
                i++
                if (!sender.instance!!.getBlock(sender.position.blockX(), sender.position.blockY() - i, sender.position.blockZ()).compare(Block.AIR))
                    break

                if (i > 3) {
                    sender.sendActionBar(Component.text("Couldn't reserve a seat", NamedTextColor.RED))
                    return@setDefaultExecutor
                }
            }

            val roundedPos = Pos(
                sender.position.blockX().toDouble(),
                sender.position.blockY().toDouble() - (i - 1),
                sender.position.blockZ().toDouble()
            )

            val armourStand = Entity(EntityType.ARMOR_STAND)
            val armourStandMeta = armourStand.entityMeta as ArmorStandMeta
            armourStandMeta.setNotifyAboutChanges(false)
            armourStandMeta.isSmall = true
            armourStandMeta.isHasNoBasePlate = true
            armourStandMeta.isMarker = true
            armourStandMeta.isInvisible = true
            armourStandMeta.setNotifyAboutChanges(true)
            armourStand.setNoGravity(true)

            val spawnPos = roundedPos.add(0.5, -0.3, 0.5)
            armourStand.setInstance(sender.instance!!, spawnPos.withYaw(sender.position.yaw))
                .thenRun {
                    armourStand.addPassenger(sender)
                }
        }
    }
}