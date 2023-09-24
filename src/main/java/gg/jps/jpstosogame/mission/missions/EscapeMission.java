package gg.jps.jpstosogame.mission.missions;

import gg.jps.jpstosogame.JpsTosoGame;
import gg.jps.jpstosogame.mission.Mission;
import gg.jps.jpstosogame.player.GamePlayer;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class EscapeMission extends Mission {

    public EscapeMission() {
        super("&c最終ミッション：エリアから脱出せよ", Sound.ITEM_GOAT_HORN_SOUND_5);

    }

    @Override
    public void start() {
        JpsTosoGame.getInstance().getGame().ifPresent(tosoGame -> tosoGame.getPlayers(GamePlayer.class).forEach(GamePlayer::unFreeze));
    }

    @EventHandler
    private void on(PlayerMoveEvent event) {
        final GamePlayer player = JpsTosoGame.getInstance().getPlayer(event.getPlayer());

        JpsTosoGame.getInstance().getGame().ifPresent(tosoGame -> {
            if (tosoGame.isInGoalArea(player.getPlayer())) {
                player.firework();
                player.sendTitle("&b脱出に成功しました", "");
                tosoGame.teleportGoalLocation(player);
            }
        });
    }
}
