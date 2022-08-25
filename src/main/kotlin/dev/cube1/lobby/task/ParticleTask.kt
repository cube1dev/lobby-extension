package dev.cube1.lobby.task

import dev.cube1.lobby.listener.Listener.instance
import kotlinx.coroutines.*
import net.minestom.server.coordinate.Vec
import world.cepi.particle.Particle
import world.cepi.particle.ParticleType
import world.cepi.particle.data.OffsetAndSpeed
import world.cepi.particle.extra.Dust
import world.cepi.particle.renderer.Renderer
import world.cepi.particle.showParticle

@OptIn(DelicateCoroutinesApi::class)
object ParticleTask {

    val particles = listOf(
        Pair(Vec(39.5, 113.5, 5.5), Vec(41.5, 113.5, 7.5)),
        Pair(Vec(13.5, 112.5, -0.5), Vec(15.5, 112.5, -2.5)))

    fun run() {
        GlobalScope.launch {
            while(isActive) {
                particles.forEach { vecPair ->
                    instance.showParticle(
                        Particle.particle(
                            type = ParticleType.DUST,
                            data = OffsetAndSpeed(),
                            extraData = Dust(0f, 0.6f, 1f, 1f),
                            count = 1
                        ),
                        Renderer.fixedRectangle(vecPair.first, vecPair.second, step = 0.5)
                    )
                }

                delay(100)
            }
        }
    }

}