package dev.cube1.lobby

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
            Tag.NBT<NBTCompound>("Color"),
            Tag.NBT<NBTCompound>("GlowingText"),
            Tag.NBT<NBTCompound>("Text1"),
            Tag.NBT<NBTCompound>("Text2"),
            Tag.NBT<NBTCompound>("Text3"),
            Tag.NBT<NBTCompound>("Text4")
        )

}