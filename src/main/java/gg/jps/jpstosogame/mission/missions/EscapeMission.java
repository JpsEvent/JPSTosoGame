package gg.jps.jpstosogame.mission.missions;

import gg.jps.jpstosogame.JpsTosoGame;
import gg.jps.jpstosogame.mission.Mission;
import gg.jps.jpstosogame.player.GamePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.concurrent.TimeUnit;

public class EscapeMission extends Mission {

    public EscapeMission() {
        super("&c最終ミッション：エリアから脱出せよ", Sound.ITEM_GOAT_HORN_SOUND_5);

    }

    @Override
    public void start() {
        JpsTosoGame.getInstance().getGame().ifPresent(tosoGame -> {
            tosoGame.getTosoEscapeTime().start();
            tosoGame.getPlayers(GamePlayer.class).forEach(GamePlayer::unFreeze);
            JpsTosoGame.getInstance().syncScheduler().after(tosoGame.getConfig().getFlyTime(), TimeUnit.SECONDS).run(tosoGame::creativeMode);
        });
    }

    @EventHandler
    private void on(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        JpsTosoGame.getInstance().getGame().ifPresent(tosoGame -> {
            if (tosoGame.isInGoalArea(player)) {
                tosoGame.getInGameHandler().goal(tosoGame, player);
            }
        });
    }

    @Override
    public void onFailed() {

        super.onFailed();
    }
}
