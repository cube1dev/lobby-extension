package dev.cube1.lobby.task

import dev.cube1.lobby.listener.Listener.instance
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import net.minestom.server.coordinate.Vec
import world.cepi.particle.Particle
import world.cepi.particle.ParticleType
import world.cepi.particle.data.OffsetAndSpeed
import world.cepi.particle.extra.Dust
import world.cepi.particle.renderer.Renderer
import world.cepi.particle.showParticle

object ParticleTask {

    val particles = listOf(
        Pair(Vec(22.5, 223.5, -2.5), Vec(28.5, 223.5, 3.5)),
        Pair(Vec(-2.5, 223.5, -21.5), Vec(3.5, 223.5, -27.5)))

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