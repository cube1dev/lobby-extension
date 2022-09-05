package dev.cube1.lobby.handler

import net.kyori.adventure.key.Key
import net.minestom.server.instance.block.BlockHandler
import net.minestom.server.tag.Tag
import net.minestom.server.utils.NamespaceID

class CampfireHandler : BlockHandler {
    override fun getNamespaceId(): NamespaceID = NamespaceID.from(Key.key("minecraft:campfire"))

    override fun getBlockEntityTags(): MutableCollection<Tag<*>> = mutableListOf(
            Tag.NBT("Items")
        )
}