package gg.jps.jpstosogame.mission.missions;

import gg.jps.jpstosogame.JpsTosoGame;
import gg.jps.jpstosogame.data.LocationData;
import gg.jps.jpstosogame.game.TosoGame;
import gg.jps.jpstosogame.mission.Mission;
import gg.jps.jpstosogame.player.GamePlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakCoreMission extends Mission {

    private final TosoGame game;

    public BreakCoreMission(TosoGame game) {
        super("&e第2ミッション： ハンター強化イベントを阻止せよ", Sound.ITEM_GOAT_HORN_SOUND_4);
        this.game = game;
    }

    public void placeObsidianLocations() {
        game.getConfig().getCoreLocations().stream().map(LocationData::getLocation).forEach(location -> location.getWorld().setType(location, Material.OBSIDIAN));
    }

    public void onFailed() {
        if (isAllBreakCore()) return;

        game.sound(Sound.ITEM_GOAT_HORN_SOUND_5);
        game.title("&c【ミッション】ハンター強化ミッション終了",  "");
        game.title( "&c ハンターに俊足能力が付与されました。", "");

        strongHunters();
    }

    @EventHandler
    private void on(BlockBreakEvent event) {
        final Block brokenBlock = event.getBlock();
        if (event.getPlayer().getInventory().getItemInMainHand() == null) return;
        if (event.getPlayer().getInventory().getItemInMainHand().getType() != Material.DIAMOND_PICKAXE) return;
        if (brokenBlock.getType() != Material.OBSIDIAN) return;

        brokenBlock.getWorld().spawnEntity(brokenBlock.getLocation(), EntityType.FIREWORK);

        if (isAllBreakCore()) {
            game.sound(Sound.ITEM_GOAT_HORN_SOUND_1);
            game.broadcast("&b%sが最後のコアを破壊しました。");
            game.title("&b第2ミッションクリア", "");

            return;
        }

        game.sound(Sound.BLOCK_NOTE_BLOCK_HARP);
        game.broadcast(String.format("&b%sがコアを破壊しました。", event.getPlayer().getName()));
    }

    public void strongHunters() {
        game.getHunterPlayers().forEach(uuid -> JpsTosoGame.getInstance().getPlayer(uuid).ifPresent(GamePlayer::strong));
    }

    private boolean isAllBreakCore() {
        for (LocationData locationData : game.getConfig().getCoreLocations()) {
            final Location location = locationData.getLocation();

            if (location.getBlock() == null) continue;
            final Block block = location.getBlock();
            if (block.getType() == Material.OBSIDIAN) {
                return true;
            }
        }

        return false;
    }
}
