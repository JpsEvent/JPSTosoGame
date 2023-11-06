package gg.jps.jpstosogame.game;

import gg.jps.jpstosogame.JpsTosoGame;
import gg.jps.jpstosogame.util.TimeFormat;
import org.bukkit.Sound;
import pakira.scheduler.Scheduler;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class TosoEscapeTime {

    private final TosoGame game;
    private final Scheduler scheduler;

    private final AtomicLong timer = new AtomicLong();

    public TosoEscapeTime(TosoGame game) {
        this.game = game;
        scheduler = new Scheduler(JpsTosoGame.getInstance(), false);
    }

    public void start() {
        scheduler.every(1, TimeUnit.SECONDS).run(() -> {
            final long finishTime = game.getConfig().getFinishTime();
            final long timeLeft = (finishTime - timer.getAndIncrement());
            final String formattedTimeLeft = TimeFormat.format((int) timeLeft);

            if (timeLeft == 0) {
                game.getBossBar().hide();
                game.title("&dゲーム終了", "");
                game.sound(Sound.ENTITY_ENDER_DRAGON_DEATH);
                game.nextHandler();
                scheduler.cancel();
                return;
            }

            game.getBossBar().setBossBar(String.format("残り時間: %s", formattedTimeLeft));
        });
    }

    public void cancel() {
        scheduler.cancel();
    }
}
