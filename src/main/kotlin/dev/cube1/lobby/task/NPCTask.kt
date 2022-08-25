package dev.cube1.lobby.task

import dev.cube1.lobby.listener.Listener
import net.kyori.adventure.text.Component
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Entity
import net.minestom.server.entity.fakeplayer.FakePlayer
import java.util.*

data class ServerNPC(val name: String, val server: String, val loc: Pos, val uniqueId: UUID = UUID.randomUUID())

object NPCTask {
    val entityList = mutableListOf(
        ServerNPC(
            "야생",
            "wild",
            Pos(43.5, 113.0, 17.5, -180F, 0F),
            UUID.fromString("fc12673f-5006-4d94-95dd-0179ff2a620b")
        )
    )

    fun run() {
        entityList.forEach { npc ->
            FakePlayer.initPlayer(npc.uniqueId, npc.name, null)
            val entity = Entity.getEntity(npc.uniqueId)
            entity?.setInstance(Listener.instance, npc.loc)
        }
    }
}