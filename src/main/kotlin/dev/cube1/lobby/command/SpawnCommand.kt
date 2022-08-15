package dev.cube1.lobby.command

import dev.cube1.lobby.listener.Listener
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Player

object SpawnCommand : Command("spawn", "스폰") {
    init {
        setDefaultExecutor { sender, _ ->
            if (sender !is Player) {
                sender.sendMessage(Component.text("This command is player only", NamedTextColor.RED))
                return@setDefaultExecutor
            }

            sender.teleport(Listener.spawn)
            sender.sendMessage(Component.text("스폰으로 이동 했어요.", NamedTextColor.GREEN))
        }
    }
}