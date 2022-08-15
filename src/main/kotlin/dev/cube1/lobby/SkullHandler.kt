package dev.cube1.lobby

import net.minestom.server.instance.block.BlockHandler
import net.minestom.server.utils.NamespaceID
import net.kyori.adventure.key.Key
import net.minestom.server.tag.Tag
import org.jglrxavpok.hephaistos.nbt.NBTCompound

class SkullHandler: BlockHandler {
    override fun getNamespaceId(): NamespaceID =
        NamespaceID.from(Key.key("minecraft:skull"))

    override fun getBlockEntityTags(): MutableCollection<Tag<*>> =
        mutableListOf(
            Tag.String("ExtraType"),
            Tag.NBT("SkullOwner")
        )
}