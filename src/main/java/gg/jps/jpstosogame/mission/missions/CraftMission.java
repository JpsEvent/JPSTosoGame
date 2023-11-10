package gg.jps.jpstosogame.mission.missions;

import gg.jps.jpstosogame.JpsTosoGame;
import gg.jps.jpstosogame.game.TosoGame;
import gg.jps.jpstosogame.mission.Mission;
import gg.jps.jpstosogame.player.GamePlayer;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pakira.player.OnlinePlayer;

import java.util.Random;

public class CraftMission extends Mission {

    private final Random random = new Random();

    public CraftMission() {
        super("&e第3ミッション：脱出のカギをゲットせよ", Sound.ITEM_GOAT_HORN_SOUND_4);
    }

    @Override
    public void start() {
        if (JpsTosoGame.getInstance().getGame().isEmpty()) return;
        final TosoGame game = JpsTosoGame.getInstance().getGame().get();

        game.getPlayers().stream().filter(player -> !game.isHunter(player.getPlayer())).map(OnlinePlayer::getUniqueId).forEach(uuid -> {
            JpsTosoGame.getInstance().getPlayer(uuid).ifPresent(gamePlayer -> giveWoolRandomPlayer(gamePlayer.getPlayer()));
        });
    }

    @Override
    public void onFailed() {
        if (JpsTosoGame.getInstance().getGame().isEmpty()) return;
        final TosoGame game = JpsTosoGame.getInstance().getGame().get();

        game.getPlayers(GamePlayer.class).forEach(player -> {
            if (game.isHunter(player.getPlayer())) return;
            if (game.isSpectator(player.getPlayer())) return;
            if (player.getPlayer().getGameMode() == GameMode.CREATIVE) return;
            if (player.getPlayer().getInventory().contains(Material.TRIPWIRE_HOOK)) return;
            game.teleportPrisonLocation(player);
        });

        game.getPlayers(GamePlayer.class).forEach(player -> {
            if (game.isSpectator(player.getPlayer())) return;
            if (player.getPlayer().getGameMode() == GameMode.CREATIVE) return;
            if (player.getPlayer().getGameMode() == GameMode.SPECTATOR) return;
            player.freeze();
        });
    }

    public void giveWoolRandomPlayer(Player player) {
        if(random.nextBoolean()) {
            player.sendMessage("赤羊毛をゲットしました。");
            player.getInventory().addItem(new ItemStack(Material.RED_WOOL));
        } else {
            player.sendMessage("青羊毛をゲットしました。");
            player.getInventory().addItem(new ItemStack(Material.BLUE_WOOL));
        }
    }

}
