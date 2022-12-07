package dev.cube1.lobby.task

import dev.cube1.lobby.listener.Listener.instance
import dev.cube1.lobby.util.toMini
import net.kyori.adventure.text.Component
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Entity
import net.minestom.server.entity.EntityType
import net.minestom.server.entity.PlayerSkin
import net.minestom.server.entity.fakeplayer.FakePlayer
import java.util.*

data class ServerNPC(
    val name: Component,
    val server: String,
    val loc: Pos,
    val uniqueId: UUID = UUID.randomUUID()
)

object NPCTask {
    val entityList = mutableListOf(
        ServerNPC(
            "<bold><green>야생 [CLOSED]".toMini(),
            "wild",
            Pos(43.5, 113.0, 17.5, -180F, 0F)
        ),
        ServerNPC(
            "<bold><aqua>Asdf 서버".toMini(),
            "asdf",
            Pos(48.5, 113.0, 3.5, 0F, 0F)
        ),
        ServerNPC(
            "<bold><green>프로젝트의 놀이터 서버".toMini(),
            "playground",
            Pos(51.5, 113.0, 22.5, -180F, 0F)
        )
    )

    fun run() {
        entityList.forEach { npc ->
            val entity = Entity(EntityType.VILLAGER).let {
                val meta = it.entityMeta
                meta.customName = npc.name
                meta.isCustomNameVisible = true

                it
            }

            entity.setInstance(instance, npc.loc)
        }
    }
}