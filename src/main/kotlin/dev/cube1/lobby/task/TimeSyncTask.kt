package dev.cube1.lobby.task

import dev.cube1.lobby.listener.Listener
import dev.cube1.lobby.util.TimeAdapter
import org.shredzone.commons.suncalc.SunTimes
import java.time.Instant
import java.time.temporal.ChronoUnit

object TimeSyncTask : Runnable {
    private var latitude = 0.0
    private var longitude = 0.0
    private lateinit var timeAdapter: TimeAdapter

    fun updateTimeAdapter(now: Instant) {
        val todaySunTimes: SunTimes = SunTimes.compute().on(now).midnight().at(latitude, longitude).execute()
        val todaySunrise = todaySunTimes.rise!!.toInstant()

        if (now.isBefore(todaySunrise)) { //자정 이후 일출 이전
            val yesterday = now.minus(1, ChronoUnit.DAYS)
            val yesterdaySunTimes = SunTimes.compute().on(yesterday).midnight().at(latitude, longitude).execute()
            val yesterdaySunset = yesterdaySunTimes.set!!.toInstant()
            timeAdapter = TimeAdapter(yesterdaySunset, todaySunrise, TimeAdapter.Type.NIGHT)
            return
        }

        val todaySunset = todaySunTimes.set!!.toInstant()

        if (now.isBefore(todaySunset)) { // 일출 이후 일몰 이전
            timeAdapter = TimeAdapter(todaySunrise, todaySunset, TimeAdapter.Type.DAY)
            return
        }

        // 일몰 이후 자정 이전
        val tomorrow = now.plus(1, ChronoUnit.DAYS)
        val tomorrowSunTimes = SunTimes.compute().on(tomorrow).midnight().at(latitude, longitude).execute()
        val tomorrowSunrise = tomorrowSunTimes.rise!!.toInstant()
        timeAdapter = TimeAdapter(todaySunset, tomorrowSunrise, TimeAdapter.Type.NIGHT)
    }

    private var lastTick: Long = 0

    override fun run() {
        val now = Instant.now()
        if (!timeAdapter.isValid(now)) updateTimeAdapter(now)
        val tick: Long = timeAdapter.currentTick(now)

        if (lastTick != tick) {
            lastTick = tick
            Listener.instance.time = tick
        }
    }
}