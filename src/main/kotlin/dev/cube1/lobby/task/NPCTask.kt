package dev.cube1.lobby.task

import dev.cube1.lobby.listener.Listener.instance
import dev.cube1.lobby.util.toMini
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Entity
import net.minestom.server.entity.EntityType
import java.util.*

data class ServerNPC(val name: Component, val server: String, val loc: Pos, val uniqueId: UUID = UUID.randomUUID())

object NPCTask {
    val entityList = mutableListOf(
        ServerNPC(
            "<bold><green>야생".toMini(),
            "wild",
            Pos(43.5, 113.0, 17.5, -180F, 0F),
        )
    )

    fun run() {
        entityList.forEach { npc ->
            val entity = Entity(EntityType.VILLAGER).let { e ->
                val meta = e.entityMeta
                meta.customName = npc.name
                meta.isCustomNameVisible = true

                e
            }
            entity.setInstance(instance, npc.loc)
        }
    }
}