package gg.jps.jpstosogame.game;

import gg.jps.jpstosogame.data.CuboidData;
import gg.jps.jpstosogame.data.LocationData;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class TosoConfig {

    private final LocationData waitingLocation;
    private final CuboidData goalArea;
    private final CuboidData prisonArea;
    private final Set<LocationData> explosionHunterBoxes;
    // private final Set<LocationData> coreLocations;
    private final long gameTime;
    /* private final long placeBlockMissionTime;
    private final long breakMissionTime;
    private final long craftMissionTime;
    private final long breakFailedTime; */
    private final long lavaStopMissionTime;
    private final long lavaStopFailedTime;
    private final long diamondGetMissionTime;
    private final long diamondGetFailedTime;
    private final long finishTime;
    private final long flyTime;
    private LocationData prisonLocation;
    private LocationData openingGameLocation;
    private LocationData goalLocation;
    private LocationData lavaStopLever;
    private LocationData lavaLocation;
    private LocationData diamondBlockLocation;
    private int lottery;


    public TosoConfig(LocationData waitingLocation) {
        this.waitingLocation = waitingLocation;
        this.prisonLocation = waitingLocation;
        this.openingGameLocation = waitingLocation;
        this.goalLocation = waitingLocation;
        this.explosionHunterBoxes = Set.of(waitingLocation);
        // this.coreLocations = Set.of(waitingLocation);
        this.lavaStopLever = waitingLocation;
        this.diamondBlockLocation = waitingLocation;
        this.goalArea = new CuboidData(waitingLocation, waitingLocation);
        this.prisonArea = new CuboidData(waitingLocation, waitingLocation);
        this.gameTime = 60;
        this.lavaStopMissionTime = 50;
        this.lavaStopFailedTime = 20;
        this.diamondGetMissionTime = 40;
        this.diamondGetFailedTime = 10;
        // this.placeBlockMissionTime = 50;
        // this.breakMissionTime = 40;
        // this.craftMissionTime = 30;
        // this.breakFailedTime = 10;
        this.finishTime = 30;
        this.flyTime = 10;
        this.lottery = 10;
    }

    /*public void setCoreLocations(LocationData coreLocations) {
        this.coreLocations.add(coreLocations);
    }*/
}
