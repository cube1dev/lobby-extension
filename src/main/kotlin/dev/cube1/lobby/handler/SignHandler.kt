package dev.cube1.lobby.handler

import net.minestom.server.instance.block.BlockHandler
import net.minestom.server.utils.NamespaceID
import net.kyori.adventure.key.Key
import net.minestom.server.tag.Tag
import org.jglrxavpok.hephaistos.nbt.NBTCompound

class SignHandler: BlockHandler {

    override fun getNamespaceId(): NamespaceID =
        NamespaceID.from(Key.key("minecraft:sign"))

    override fun getBlockEntityTags(): MutableCollection<Tag<*>> =
        mutableListOf(
            Tag.String("ExtraType"),
            Tag.NBT("Color"),
            Tag.NBT("GlowingText"),
            Tag.NBT("Text1"),
            Tag.NBT("Text2"),
            Tag.NBT("Text3"),
            Tag.NBT("Text4")
        )

}