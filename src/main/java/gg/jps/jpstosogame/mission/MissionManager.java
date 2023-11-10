package gg.jps.jpstosogame.mission;

import gg.jps.jpstosogame.JpsTosoGame;
import gg.jps.jpstosogame.game.TosoGame;
import gg.jps.jpstosogame.mission.missions.BreakCoreMission;
import gg.jps.jpstosogame.mission.missions.CraftMission;
import gg.jps.jpstosogame.mission.missions.EscapeMission;
import gg.jps.jpstosogame.mission.missions.PlaceBlockMission;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
public class MissionManager {

    private final TosoGame game;
    private final BreakCoreMission breakCoreMission;
    private final CraftMission craftMission;
    private final EscapeMission escapeMission;
    private final PlaceBlockMission placeBlockMission;


    public MissionManager(TosoGame game) {
        this.game = game;
        this.breakCoreMission = new BreakCoreMission(game);
        this.craftMission = new CraftMission();
        this.escapeMission = new EscapeMission();
        this.placeBlockMission = new PlaceBlockMission(game);

        // このクラス賢くコーディングできるけど割愛ｗ
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
    }

    public void startCraftMission() {
        craftMission.start();
        announce(craftMission);
        onFailedAfter(breakCoreMission, game.getConfig().getCraftFailedTime());
    }
    public void startEscapeMission() {
        escapeMission.start();
        announce(escapeMission);
    }
    public void startPlaceBlockMission() {
        placeBlockMission.start();
        announce(placeBlockMission);
    }

}
