package gg.jps.jpstosogame.game.handler;


import gg.jps.jpstosogame.JpsTosoGame;
import gg.jps.jpstosogame.data.LocationData;
import gg.jps.jpstosogame.game.TosoGame;
import gg.jps.jpstosogame.player.GamePlayer;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import pakira.game.Handler;
import pakira.player.OnlinePlayer;

import java.util.concurrent.TimeUnit;

public class WaitingHandler extends Handler {

    private final TosoGame game;

    private boolean isLottery;

    public WaitingHandler(TosoGame game) {
        super(game);
        this.game = game;
    }

    @Override
    public TosoGame getGame() {
        return game;
    }

    @Override
    public void join(OnlinePlayer player) {
        // 待機している間は待機所にテレポート
        player.getPlayer().getActivePotionEffects().clear();
        getGame().teleportWaitingLocation((GamePlayer) player);
    }

    @EventHandler
    private void onDamage(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onBlockClick(PlayerInteractEvent event) {
        final Block clickedBlock = event.getClickedBlock();
        final GamePlayer player = JpsTosoGame.getInstance().getPlayer(event.getPlayer());

        if (clickedBlock == null || event.getHand() != EquipmentSlot.HAND) return;

        switch (clickedBlock.getType()) {
            case EMERALD_BLOCK -> {
                getGame().addEscape(player);
                getGame().teleportOpeningGameLocation(player);
            }
            case REDSTONE_BLOCK -> {
                getGame().addEscape(player);
                getGame().teleportPrisonLocation(player);
            }
            case DIAMOND_BLOCK -> randomHunterChoose(player);
        }
    }

    private void randomHunterChoose(GamePlayer player) {
        if (isLottery) return;
        isLottery = true;
        // 3秒後に実行
        broadcast(String.format("%s 抽選中・・・", player.getUsername()));
        JpsTosoGame.getInstance().syncScheduler().after(3, TimeUnit.SECONDS).run(() -> {
            // 始まっていたら抽選をしない
            if (getGame().getCurrentHandler() instanceof InGameHandler) return;
            if (isOut()) {
                getGame().broadcast("&cアウト");
                sound(Sound.ENTITY_WITHER_SPAWN);
                explosionHunterBoxesAndFirework();
                startGame();

                return;
            }
            // セーフな場合
            broadcast("&bセーフ");
            isLottery = false;
        });
    }

    private void startGame() {
        nextHandlerAndStart();
    }

    private void explosionHunterBoxesAndFirework() {
        getGame().getConfig().getExplosionHunterBoxes().stream().map(LocationData::getLocation).forEach(location -> {
            location.createExplosion(1);
            location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        });
    }

    private boolean isOut() {
        return Math.random() < ((double) game.getConfig().getLottery() / 100);
    }
}
