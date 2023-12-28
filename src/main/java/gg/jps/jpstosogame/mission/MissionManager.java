package gg.jps.jpstosogame.mission;

import gg.jps.jpstosogame.JpsTosoGame;
import gg.jps.jpstosogame.game.TosoGame;
import gg.jps.jpstosogame.mission.missions.BreakCoreMission;
import gg.jps.jpstosogame.mission.missions.CraftMission;
import gg.jps.jpstosogame.mission.missions.EscapeMission;
import gg.jps.jpstosogame.mission.missions.PlaceBlockMission;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.concurrent.TimeUnit;

@Getter
public class MissionManager implements Listener {

    private final TosoGame game;
    private final BreakCoreMission breakCoreMission;
    private final CraftMission craftMission;
    private final EscapeMission escapeMission;
    private final PlaceBlockMission placeBlockMission;

    private Mission currentMission;


    public MissionManager(TosoGame game) {
        this.game = game;
        this.breakCoreMission = new BreakCoreMission(game);
        this.craftMission = new CraftMission();
        this.escapeMission = new EscapeMission();
        this.placeBlockMission = new PlaceBlockMission(game);

        Bukkit.getPluginManager().registerEvents(this, JpsTosoGame.getInstance());
    }

    @EventHandler
    private void onPlace(BlockPlaceEvent event) {
        if (currentMission instanceof PlaceBlockMission)
            placeBlockMission.onPlace(event);
    }

    @EventHandler
    private void onBreak(BlockBreakEvent event) {
        if (currentMission instanceof BreakCoreMission)
            breakCoreMission.onBreak(event);
    }

    @EventHandler
    private void onMove(PlayerMoveEvent event) {
        if (currentMission instanceof EscapeMission)
            escapeMission.onMove(event);
    }

    private void onFailedAfter(Mission mission, long after) {
        onFailedAfter(mission, after, TimeUnit.SECONDS);
    }

    private void onFailedAfter(Mission mission, long after, TimeUnit unit) {
        JpsTosoGame.getInstance().syncScheduler().after(after, unit).run(mission::onFailed);
    }

    private void announce(Mission mission) {
        game.title(mission.getTitle(), "");
        game.sound(mission.getSound());
        game.broadcast(mission.getTitle());
    }

    public void startBreakMission() {
        breakCoreMission.start();
        announce(breakCoreMission);
        onFailedAfter(breakCoreMission, game.getConfig().getBreakFailedTime());
        currentMission = breakCoreMission;
    }

    public void startCraftMission() {
        craftMission.start();
        announce(craftMission);
        currentMission = craftMission;
    }

    public void startEscapeMission() {
        escapeMission.start();
        announce(escapeMission);
        currentMission = escapeMission;
    }

    public void startPlaceBlockMission() {
        placeBlockMission.start();
        announce(placeBlockMission);
        currentMission = placeBlockMission;
    }

}
