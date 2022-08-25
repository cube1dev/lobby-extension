package dev.cube1.lobby.task

import dev.cube1.lobby.listener.Listener.instance
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Entity
import net.minestom.server.entity.EntityType
import java.util.*

data class ServerNPC(val name: TextComponent, val server: String, val loc: Pos, val uniqueId: UUID = UUID.randomUUID())

object NPCTask {
    val entityList = mutableListOf(
        ServerNPC(
            Component.text("야생"),
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