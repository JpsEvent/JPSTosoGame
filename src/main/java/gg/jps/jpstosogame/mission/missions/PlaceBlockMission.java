package gg.jps.jpstosogame.mission.missions;

import gg.jps.jpstosogame.game.TosoGame;
import gg.jps.jpstosogame.mission.Mission;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceBlockMission extends Mission {

    private final TosoGame game;

    public PlaceBlockMission(TosoGame game) {
        super("&e第1ミッション: 捕まった仲間を救出せよ", Sound.ITEM_GOAT_HORN_SOUND_4);
        this.game = game;
    }

    @EventHandler
    private void on(BlockPlaceEvent event) {
        final Block placedBlock = event.getBlockPlaced();
        final Block bottomBlock = placedBlock.getRelative(BlockFace.DOWN);

        if (placedBlock.getType() != Material.POLISHED_BLACKSTONE_PRESSURE_PLATE)
        if (bottomBlock == null) return;
        if (bottomBlock.getType() != Material.POLISHED_ANDESITE) {
            event.getPlayer().sendMessage(ChatColor.RED + "牢屋前の安山岩の上に置くことができます！");
            event.setCancelled(true);

            return;
        }

        placedBlock.getWorld().spawnEntity(placedBlock.getLocation(), EntityType.FIREWORK);
        game.broadcast(String.format("&b%sが感圧版を設置した", event.getPlayer().getName()));

        // 3つすべてに置かれたとき
        if (isAllPlaced()) {
            game.broadcast("&b牢屋が開放した");
            game.sound(Sound.ITEM_GOAT_HORN_SOUND_1);
        }

    }

    private boolean isAllPlaced() {
        return false;
    }
}
