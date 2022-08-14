package dev.cube1.lobby.command

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Player

class FlyCommand : Command("fly") {
    init {
        setDefaultExecutor { sender, _ ->
            if (sender !is Player) {
                sender.sendMessage(Component.text("This command is player only", NamedTextColor.RED))
                return@setDefaultExecutor
            }

            if (!sender.isAllowFlying) {
                sender.sendMessage(Component.text("비행을 활성화 했어요.", NamedTextColor.GREEN))
            } else {
                sender.sendMessage(Component.text("비행을 비활성화 했어요.", NamedTextColor.RED))
            }

            sender.isAllowFlying = !sender.isAllowFlying
            sender.isFlying = sender.isAllowFlying
        }
    }
}