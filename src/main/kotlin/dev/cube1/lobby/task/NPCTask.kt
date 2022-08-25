package dev.cube1.lobby.task

import dev.cube1.lobby.util.toMini
import net.kyori.adventure.text.Component
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.PlayerSkin
import net.minestom.server.entity.fakeplayer.FakePlayer
import java.util.*

data class ServerNPC(val name: Component, val server: String, val loc: Pos, val uniqueId: UUID = UUID.randomUUID())

object NPCTask {
    val entityList = mutableListOf(
        ServerNPC(
            "<bold><green>야생".toMini(), "wild", Pos(43.5, 113.0, 17.5, -180F, 0F)
        )
    )

    fun run() {
        entityList.forEach { npc ->
            FakePlayer.initPlayer(UUID.randomUUID(), npc.server) { fakePlayer ->
                fakePlayer.isInvisible = true
                fakePlayer.skin = PlayerSkin.fromUsername("Plaming")
                val meta = fakePlayer.entityMeta
                meta.customName = npc.name
                meta.isCustomNameVisible = true

                fakePlayer.respawnPoint = npc.loc
            }
        }
    }
}