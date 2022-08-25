package dev.cube1.lobby.task

import dev.cube1.lobby.listener.Listener
import net.kyori.adventure.text.Component
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Entity
import net.minestom.server.entity.EntityType

data class ServerNPC(val name: String, val server: String, val loc: Pos)

object NPCTask {
    val entityList = mutableListOf(
        ServerNPC("Project_TL", "wild", Pos(43.5, 113.0, 17.5, -180F, 0F))
    )

    fun run() {
        val spawnPosition = Pos(45.5, 115.0, 17.5)
        val horse = Entity(EntityType.HORSE)
        horse.setInstance(Listener.instance, spawnPosition)

        entityList.forEach { npc ->
            val entity = Entity(EntityType.PLAYER).let {
                val meta = it.entityMeta
                meta.customName = Component.text(npc.name)
                meta.isCustomNameVisible = false

                it
            }
            entity.setInstance(Listener.instance, npc.loc)
        }
    }
}