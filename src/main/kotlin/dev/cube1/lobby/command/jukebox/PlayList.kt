package dev.cube1.lobby.command.jukebox

import dev.cube1.lobby.command.loadedNbs
import dev.emortal.nbstom.NBS
import kotlinx.coroutines.*
import net.minestom.server.entity.Player

class Playlist(val player: Player, val list: ArrayList<Int> = ArrayList(), var nowPlaying: Int = -1, var currentTick: Int = 0, private var _loop: Boolean = false, var playing: Boolean = false) {
    var playJob: Job? = null

    var loop
        get() = _loop
        set(value) {
            if(value && nowPlaying == -1)
                play()

            if(!value && nowPlaying != -1)
                stop()

            _loop = value
        }

    fun play() {
        if(nowPlaying == -1) return
        playing = true

        playJob = GlobalScope.launch {
            while(isActive) {
                val song = loadedNbs[nowPlaying]
                NBS.play(song, player)
                delay((1000.0 / song.tps).toLong() * song.length)
                nowPlaying++
                if(nowPlaying + 1 >= list.size) {
                    if(!loop) {
                        playing = false
                        nowPlaying = -1
                        break
                    }
                    nowPlaying = 0
                }
            }
        }
    }

    fun stop() {
        playing = false
        playJob?.cancel()
        NBS.stopPlaying(player)
    }
}