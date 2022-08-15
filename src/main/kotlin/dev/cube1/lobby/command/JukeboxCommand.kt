package dev.cube1.lobby.command

import dev.cube1.lobby.command.jukebox.Playlist
import dev.cube1.lobby.util.getOrNew
import dev.emortal.nbstom.NBS
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Player
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import java.io.File
import java.util.LinkedList

val loadedNbs = LinkedList<NBS>()
val nbsNames = LinkedList<String>()

object JukeboxCommand: Command("jukebox") {

    val playlist = HashMap<Player, Playlist>()

    fun openAddSong(player: Player) {
        val inventory = Inventory(InventoryType.CHEST_6_ROW, Component.text("MUSIC")
            .decorate(TextDecoration.BOLD))
        val playlist = JukeboxCommand.playlist.getOrNew(player)

        inventory.addInventoryCondition { _, _, _, res ->
            res.isCancel = true
        }

        for(i in 0..35) {
            if(loadedNbs.size >= i + 1) {
                val nbs = loadedNbs[i]
                inventory.setItemStack(
                    i, ItemStack.of(Material.MUSIC_DISC_13)
                        .withDisplayName(
                            Component.text(nbsNames[i], NamedTextColor.WHITE, TextDecoration.BOLD)
                                .decoration(TextDecoration.ITALIC, false)
                        )
                )
                inventory.addInventoryCondition { _, slot, _, _ ->
                    if(slot == i) {
                        playlist.list.add(i)
                        //JukeboxCommand.playlist[player] = playlist
                        openPlaylists(player)
                    }
                }
            }
        }

        // separator
        for(i in 36..45) {
            inventory.setItemStack(i, ItemStack.of(Material.BLACK_STAINED_GLASS_PANE)
                .withDisplayName(Component.text(" ", NamedTextColor.RED)))
        }

        inventory.setItemStack(45, ItemStack.of(Material.ARROW)
            .withDisplayName(
                Component.text("이전 페이지", NamedTextColor.WHITE, TextDecoration.BOLD)
                    .decoration(TextDecoration.ITALIC, false)
            ))
        inventory.addInventoryCondition { _, slot, _, _ ->
            if(slot == 45) {
                openPlaylists(player)
            }
        }
        player.openInventory(inventory)
    }

    fun openPlaylists(player: Player) {
        val inventory = Inventory(InventoryType.CHEST_6_ROW, Component.text("PLAYLIST")
            .decorate(TextDecoration.BOLD))
        val playlist = JukeboxCommand.playlist.getOrNew(player)

        inventory.addInventoryCondition { _, _, _, res ->
            res.isCancel = true
        }

        for(i in 0..35) {
            if(playlist.list.size >= i + 1) {
                val nbs = loadedNbs[playlist.list[i]]
                inventory.setItemStack(
                    i, ItemStack.of(Material.MUSIC_DISC_13)
                        .withDisplayName(
                            Component.text(nbsNames[i], NamedTextColor.WHITE, TextDecoration.BOLD)
                                .decoration(TextDecoration.ITALIC, false)
                        )
                        .withLore(listOf(
                            Component.empty(),
                            this.run {
                                Component.text("클릭으로 재생목록에서 ", NamedTextColor.WHITE)
                                    .append(Component.text("제거", NamedTextColor.RED, TextDecoration.BOLD))
                                    .append(Component.text("하세요!", NamedTextColor.WHITE)
                                            .decoration(TextDecoration.BOLD, false))
                            }.decoration(TextDecoration.ITALIC, false),
                            Component.empty()
                        ))
                )
                inventory.addInventoryCondition { _, slot, _, _ ->
                    if(slot == i) {
                        playlist.list.removeAt(i)
                        //JukeboxCommand.playlist[player] = playlist
                        openPlaylists(player)
                    }
                }
            } else if(i == 35) {
                inventory.setItemStack(35, ItemStack.of(Material.NETHER_STAR)
                    .withDisplayName(Component.text("추가", NamedTextColor.YELLOW, TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false)))
                inventory.addInventoryCondition { _, slot, _, _ ->
                    if(slot == 35) {
                        openAddSong(player)
                    }
                }
            }
        }

        // separator
        for(i in 36..45) {
            inventory.setItemStack(i, ItemStack.of(Material.BLACK_STAINED_GLASS_PANE)
                .withDisplayName(Component.text(" ", NamedTextColor.RED)))
        }

        // control
        inventory.setItemStack(46, ItemStack.of(
            if(playlist.playing)
                Material.RED_DYE
            else
                Material.LIME_DYE
        )
            .withDisplayName(
                this.run {
                    if (playlist.playing)
                        Component.text("STOP", NamedTextColor.RED, TextDecoration.BOLD)
                    else
                        Component.text("PLAY", NamedTextColor.GREEN, TextDecoration.BOLD)
                }.decoration(TextDecoration.ITALIC, false)
            )
            .withLore(listOf(
                Component.empty(),
                this.run {
                    if (playlist.playing)
                        Component.text("클릭으로 ", NamedTextColor.WHITE)
                            .append(Component.text("정지", NamedTextColor.RED, TextDecoration.BOLD))
                            .append(Component.text("하세요!", NamedTextColor.WHITE)
                                .decoration(TextDecoration.BOLD, false))
                    else
                        Component.text("클릭으로 ", NamedTextColor.WHITE)
                            .append(Component.text("재생", NamedTextColor.GREEN, TextDecoration.BOLD))
                            .append(Component.text("하세요!", NamedTextColor.WHITE)
                                .decoration(TextDecoration.BOLD, false))
                }.decoration(TextDecoration.ITALIC, false),
                Component.empty()
            )))
        inventory.addInventoryCondition { _, slot, _, res ->
            if(slot == 46) {
                if(playlist.playing) {
                    playlist.stop()
                } else {
                    playlist.play()
                }
                //JukeboxCommand.playlist[player] = playlist
                openPlaylists(player)
            }
        }

        inventory.setItemStack(52, ItemStack.of(Material.CLOCK)
            .withDisplayName(
                Component.text("LOOP", if(playlist.loop) NamedTextColor.GREEN else NamedTextColor.RED, TextDecoration.BOLD)
                    .decoration(TextDecoration.ITALIC, false)
            )
            .withLore(listOf(
                Component.empty(),
                this.run {
                    if (playlist.loop)
                        Component.text("클릭으로 ", NamedTextColor.WHITE)
                            .append(Component.text("반복 비활성화", NamedTextColor.RED, TextDecoration.BOLD))
                            .append(Component.text("하세요!", NamedTextColor.WHITE)
                                .decoration(TextDecoration.BOLD, false))
                    else
                        Component.text("클릭으로 ", NamedTextColor.WHITE)
                            .append(Component.text("반복 활성화", NamedTextColor.GREEN, TextDecoration.BOLD))
                            .append(Component.text("하세요!", NamedTextColor.WHITE)
                                .decoration(TextDecoration.BOLD, false))
                }.decoration(TextDecoration.ITALIC, false),
            )))
        inventory.addInventoryCondition { _, slot, _, res ->
            if(slot == 52) {
                playlist.loop = !playlist.loop
                //JukeboxCommand.playlist[player] = playlist
                openPlaylists(player)
            }
        }

        inventory.setItemStack(53, ItemStack.of(Material.BARRIER)
            .withDisplayName(
                Component.text("닫기", NamedTextColor.RED, TextDecoration.BOLD)
                    .decoration(TextDecoration.ITALIC, false)
            ))
        inventory.addInventoryCondition { _, slot, _, res ->
            if(slot == 53) {
                player.closeInventory()
            }
        }

        inventory.setItemStack(45, ItemStack.of(Material.ARROW)
            .withDisplayName(
                Component.text("이전 곡", NamedTextColor.WHITE, TextDecoration.BOLD)
            ))
        inventory.setItemStack(47, ItemStack.of(Material.ARROW)
            .withDisplayName(
                Component.text("다음 곡", NamedTextColor.WHITE, TextDecoration.BOLD)
            ))
        inventory.addInventoryCondition { _, slot, _, res ->
            if(playlist.list.isEmpty()) return@addInventoryCondition
            if(playlist.nowPlaying == -1) return@addInventoryCondition

            playlist.stop()

            if(slot == 45) {
                playlist.nowPlaying--
            } else if(slot == 47) {
                playlist.nowPlaying++
            }

            if(playlist.nowPlaying + 1 >= playlist.list.size)
                playlist.nowPlaying = 0

            if(playlist.nowPlaying + 1 < 0)
                playlist.nowPlaying = playlist.list.size - 1

            playlist.play()
            //JukeboxCommand.playlist[player] = playlist
            openPlaylists(player)
        }

        player.openInventory(inventory);
    }

    init {
        File("songs/").listFiles()?.forEach { nbsFile ->
            loadedNbs.add(NBS(nbsFile.toPath()))
            nbsNames.add(nbsFile.nameWithoutExtension)
        }

        setDefaultExecutor { sender, _ ->
            if(sender !is Player) return@setDefaultExecutor

            openPlaylists(sender)
        }
    }

}