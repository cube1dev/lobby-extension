package dev.cube1.lobby.task

import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import dev.cube1.lobby.listener.Listener
import dev.cube1.lobby.util.toMini
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

object TabList {

    /*
    val uuidOfFakePlayers = ArrayListMultimap.create<Player, UUID>()

    fun addPacket(): Pair<UUID, PlayerInfoPacket> {
        val uid = UUID.randomUUID()
        // black
        val prop = listOf(PlayerInfoPacket.AddPlayer.Property("textures", "eyJ0aW1lc3RhbXAiOjE1ODU5MDYzODk1MDUsInByb2ZpbGVJZCI6ImJlY2RkYjI4YTJjODQ5YjRhOWIwOTIyYTU4MDUxNDIwIiwicHJvZmlsZU5hbWUiOiJTdFR2Iiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9lYWNjYjA3ZjA1MzZkZDgxN2MxNTI1YTNhM2Y5N2U0MjgwOTVjOWIyOTg0NjBiYzdhM2EzZTFiOGI5ZmM3MWZhIn19fQ==", "XoobQpuhUKxou21LsxxLA+08ICIiTbFUvVuuqT0cTB7ZCAhD05Xvfd8Ov7PYWgXYV0oCCKayZLKAfebYfKsvI51K7ukVVCujGa13syPTctO+j30+8gbacf+4Z+kyaJx5xo15nT9qadyoDI+6PUbhgMFCMvP1Q+/I6vJL2mgH8Zud1c/dc4+3VfCE3vh2LdkSMZ5oUfFSHD+dfCQth+38nde/29MW2yTr0hbVnPqmyueraxn7gme1Bra9kts32ZC5VfqJ3yZgvVsEEy+lzIkJdELyna4HmRyE70xoJ6MJVoa+IOSxqQjrMT/sA5GBwn53/m2Bl+VZvAUn+XsKh/kzwV6vTIzLzJfburHV+7p3kTIjIT17I0uy0SjbhO0Fe//Dy3luRTe++K1Uzi9oC74ixz4dvTnPOQVwPHMqFF+NVE43JJtmB+jaXwOFNYpGZtlrqsq751QS6/MYbW9kkaOTk3d5cF4J1QHze+NgR5dqL/8jeqIkezIxNSyAUdms+mqRendHzXCHqmblwoi/ZHpVCh32gKgPpV9rOLCGzT9BhR935rOWVpriptp1m/XaR7TaT4Scu2Nnbk3ZkolZVGDPhGpgTr+oHBX0ZjVaWeoy+E6xO3k11WTZI3cn8CETk8vkzaPen9iJEIjnsOdd9wrqWzBmOq1R2GpBQaABjYDVsu0="))
        return Pair(uid, PlayerInfoPacket(PlayerInfoPacket.Action.ADD_PLAYER,
            AddPlayer(uid, " ", prop, GameMode.SURVIVAL, 0, Component.text(" "))))
    }

    fun removePacket(uid: UUID) =
        PlayerInfoPacket(PlayerInfoPacket.Action.REMOVE_PLAYER, RemovePlayer(uid))
     */

    fun run() {
        GlobalScope.launch {
            while(isActive) {
                val rt = Runtime.getRuntime()
                Listener.instance.sendPlayerListHeaderAndFooter(
                    ("\n <gradient:#00ffff:#0091ff><bold>PROJECT_TL'S PRIVATE SERVER \n" +
                            " <reset><white>프로젝트의 개인 서버에 오신걸 환영합니다! \n").toMini(),
                    ("\n <gray>Using memory: ${String.format("%.2f", (rt.maxMemory() - (rt.maxMemory() - rt.freeMemory())) * 0.001 * 0.001 * 0.001)}GB/${String.format("%.2f", rt.maxMemory() * 0.001 * 0.001 * 0.001)}GB \n" +
                            " Lobby Server powered by <bold><gradient:#ff6c32:#ff76b6>Minestom \n").toMini()
                )
                delay(25)
            }
        }
    }

}