package dev.cube1.lobby.command

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.ConsoleSender
import net.minestom.server.command.builder.Command

class GcCommand : Command("gc") {
    init {
        setDefaultExecutor { sender, _ ->
            if(sender is ConsoleSender) {
                sender.sendMessage("GC...")
                System.gc()
                sender.sendMessage("GC Done!")
            } else {
                sender.sendMessage(Component.text("권한이 없습니다!", NamedTextColor.RED))
            }
        }
    }
}