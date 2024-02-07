package gg.jps.jpstosogame.mission;

import gg.jps.jpstosogame.JpsTosoGame;
import gg.jps.jpstosogame.game.TosoGame;
import gg.jps.jpstosogame.mission.missions.*;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.concurrent.TimeUnit;

@Getter
public class MissionManager implements Listener {

    private final TosoGame game;
    private final BreakCoreMission breakCoreMission;
    private final CraftMission craftMission;
    private final EscapeMission escapeMission;
    private final PlaceBlockMission placeBlockMission;
    private final LavaStopMission lavaStopMission;
    private final DiamondGetMission diamondGetMission;

    private Mission currentMission;


    public MissionManager(TosoGame game) {
        this.game = game;
        this.breakCoreMission = new BreakCoreMission(game);
        this.craftMission = new CraftMission();
        this.escapeMission = new EscapeMission();
        this.placeBlockMission = new PlaceBlockMission(game);
        this.lavaStopMission = new LavaStopMission(game);
        this.diamondGetMission = new DiamondGetMission(game);

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

    @EventHandler
    public void onClick(PlayerInteractEvent event){
        if (currentMission instanceof LavaStopMission)
            lavaStopMission.onClick(event);
        if (currentMission instanceof DiamondGetMission)
            diamondGetMission.onClick(event);
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

    /*public void startBreakMission() {
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

    public void startPlaceBlockMission() {
        placeBlockMission.start();
        announce(placeBlockMission);
        currentMission = placeBlockMission;
    }*/

    public void startLavaStopMission(){
        lavaStopMission.start();
        announce(lavaStopMission);
        lavaStopMission.placeLavaLocation();
        lavaStopMission.placeLeverLocation();
        currentMission = lavaStopMission;
    }

    public void startDiamondGetMission(){
        diamondGetMission.start();
        announce(diamondGetMission);
        diamondGetMission.placeDiamondBlockLocation();
        currentMission = diamondGetMission;
    }

    public void startEscapeMission() {
        escapeMission.start();
        announce(escapeMission);
        currentMission = escapeMission;
    }

}
