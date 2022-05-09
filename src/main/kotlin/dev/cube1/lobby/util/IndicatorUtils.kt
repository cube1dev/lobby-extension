package dev.cube1.lobby.util

import net.kyori.adventure.text.Component
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Entity
import net.minestom.server.entity.EntityType
import net.minestom.server.entity.metadata.other.ArmorStandMeta
import net.minestom.server.instance.Instance

fun Instance.createIndicator(text: Component, pos: Pos) {
    val armorStand = Entity(EntityType.ARMOR_STAND)
    val armorStandMeta = armorStand.entityMeta as ArmorStandMeta
    armorStandMeta.isInvisible = true
    armorStandMeta.customName = text
    armorStandMeta.isCustomNameVisible = true
    armorStandMeta.isSmall = true
    armorStandMeta.isHasNoGravity = true

    armorStand.setInstance(this, pos.sub(0.0, 0.9875, 0.0))
}