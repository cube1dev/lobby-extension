package dev.cube1.lobby.util

import dev.cube1.lobby.command.jukebox.Playlist
import net.minestom.server.entity.Player

fun HashMap<Player, Playlist>.getOrNew(key: Player): Playlist =
    this[key] ?: Playlist(key).run {
        this@getOrNew[key] = this
        this
    }