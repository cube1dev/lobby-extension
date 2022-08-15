package dev.cube1.lobby.command

import dev.cube1.lobby.listener.Listener
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.builder.Command

object DateCommand : Command("date", "날짜") {
    init {
        setDefaultExecutor { sender, _ ->
            val current = Listener.instance.time
            sender.sendMessage(Component.text("현재 마크 시간은 ", NamedTextColor.GREEN)
                .append(Component.text("$current ", NamedTextColor.YELLOW))
                .append(Component.text("tick이에요.", NamedTextColor.GREEN)))
        }
    }
}