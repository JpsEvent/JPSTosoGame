package gg.jps.jpstosogame.mission.missions;

import gg.jps.jpstosogame.JpsTosoGame;
import gg.jps.jpstosogame.data.LocationData;
import gg.jps.jpstosogame.game.TosoGame;
import gg.jps.jpstosogame.game.TosoTime;
import gg.jps.jpstosogame.mission.Mission;
import gg.jps.jpstosogame.player.GamePlayer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import pakira.util.ItemBuilder;

public class DiamondGetMission extends Mission {
    private final TosoGame game;

    public DiamondGetMission(TosoGame game) {
        super("&e第2ミッション： 報酬をアップせよ", Sound.ITEM_GOAT_HORN_SOUND_4);
        this.game = game;
        placeDiamondBlockLocation();
    }

    public void placeDiamondBlockLocation() {
        game.getConfig().getDiamondBlockLocation().getLocation().getWorld().setType(game.getConfig().getDiamondBlockLocation().getLocation(), Material.DIAMOND_BLOCK);
    }

    public void onFailed() {
        game.sound(Sound.ITEM_GOAT_HORN_SOUND_5);
        game.title("&c【ミッション】報酬をアップせよミッション終了", "");
    }

    @Override
    public void onClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getHand() != EquipmentSlot.HAND) return;
        final Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;
        if (clickedBlock.getType() != Material.DIAMOND_BLOCK) return;
        if (!game.getConfig().getDiamondBlockLocation().toString().equalsIgnoreCase(LocationData.at(clickedBlock.getLocation()).toString()))
            return;
        GamePlayer player = JpsTosoGame.getInstance().getPlayer(event.getPlayer());
        if (game.isGetDiamond(player))return;
        clickedBlock.getWorld().spawnEntity(clickedBlock.getLocation(), EntityType.FIREWORK);
        player.getPlayer().getInventory().addItem(ItemBuilder.of(Material.DIAMOND).build());
        game.broadcast(String.format("&b%sが第2ミッションクリアしました。", event.getPlayer().getName()));
        player.sendTitle("&b第2ミッションクリア", "");
        game.addDiamondGetPlayer(player);
    }

    public void strongHunters() {
        game.getHunterPlayers().forEach(uuid -> JpsTosoGame.getInstance().getPlayer(uuid).ifPresent(GamePlayer::strong));
    }
}
