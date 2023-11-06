package gg.jps.jpstosogame.listener;

import gg.jps.jpstosogame.JpsTosoGame;
import gg.jps.jpstosogame.game.TosoGame;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {

    private final TosoGame game;

    public EventListener(TosoGame game) {
        this.game = game;
        Bukkit.getPluginManager().registerEvents(this, JpsTosoGame.getInstance());
    }

    @EventHandler
    private void on(PlayerJoinEvent event) {
        game.join(JpsTosoGame.getInstance().getPlayer(event.getPlayer()));
    }

    @EventHandler
    private void on(PlayerQuitEvent event) {
        game.leave(JpsTosoGame.getInstance().getPlayer(event.getPlayer()));
    }

    @EventHandler
    private void on(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void on(BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        event.setCancelled(true);
    }

    @EventHandler
    private void on(BlockPlaceEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        final Block bottomBlock = event.getBlockPlaced().getRelative(BlockFace.DOWN);
        if (bottomBlock.getType() == Material.POLISHED_ANDESITE && event.getBlock().getType() == Material.BLACKSTONE) return;
        event.setCancelled(true);
    }
}
