package dev.cube1.lobby.util

import java.time.Instant

class TimeAdapter(val from: Instant, val to: Instant, type: Type) {
    val tickOffset: Int = type.offset
    val tickDuration: Int = type.period

    fun isValid(now: Instant): Boolean {
        return now.isAfter(from) && now.isBefore(to)
    }

    fun currentTick(now: Instant): Long {
        val from: Long = from.toEpochMilli()
        val period: Long = to.toEpochMilli() - from
        val time = now.toEpochMilli()
        val current = time - from
        val tick = tickDuration * current / period
        return tickOffset + tick
    }

    enum class Type(val offset: Int, val period: Int) {
        DAY(22850, 14300), NIGHT(37150, 9700);
    }

}