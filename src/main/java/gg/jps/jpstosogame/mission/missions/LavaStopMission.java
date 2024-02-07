package gg.jps.jpstosogame.mission.missions;

import gg.jps.jpstosogame.JpsTosoGame;
import gg.jps.jpstosogame.game.TosoGame;
import gg.jps.jpstosogame.mission.Mission;
import gg.jps.jpstosogame.player.GamePlayer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class LavaStopMission extends Mission {
    private final TosoGame game;
    private boolean stopLava;

    public LavaStopMission(TosoGame game) {
        super("&e第1ミッション： コンガを止めろ", Sound.ITEM_GOAT_HORN_SOUND_4);
        this.game = game;
        this.stopLava = false;
    }

    public void placeLavaLocation() {
        game.getConfig().getLavaLocation().getLocation().getWorld().setType(game.getConfig().getLavaLocation().getLocation(), Material.LAVA);
    }

    public void placeLeverLocation() {
        game.getConfig().getLavaStopLever().getLocation().getWorld().setType(game.getConfig().getLavaStopLever().getLocation(), Material.LEVER);
    }

    public void onFailed() {
        if (stopLava) {
            game.broadcast("&eミッション成功");
            game.title("&c【ミッション】コンガを止めろミッション終了", "");
            return;
        }

        game.sound(Sound.ITEM_GOAT_HORN_SOUND_5);
        game.title("&c【ミッション】コンガを止めろミッション終了", "");
        game.title("&cハンターに俊足能力が付与されました。", "");

        strongHunters();
    }

    @Override
    public void onClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getHand() != EquipmentSlot.HAND) return;
        final Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;
        if (clickedBlock.getType() != Material.LEVER) return;
        if (this.stopLava) return;
        clickedBlock.getWorld().spawnEntity(clickedBlock.getLocation(), EntityType.FIREWORK);

        this.stopLava = true;

        game.sound(Sound.ENTITY_ENDER_DRAGON_AMBIENT);
        game.broadcast(String.format("&b%sがマグマの塞き止めに成功しました。", event.getPlayer().getName()));
        game.title("&b第2ミッションクリア", "");
    }

    public void strongHunters() {
        game.getHunterPlayers().forEach(uuid -> JpsTosoGame.getInstance().getPlayer(uuid).ifPresent(GamePlayer::strong));
    }
}
