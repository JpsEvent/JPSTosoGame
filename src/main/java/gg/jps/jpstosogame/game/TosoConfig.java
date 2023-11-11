package gg.jps.jpstosogame.game;

import gg.jps.jpstosogame.data.CuboidData;
import gg.jps.jpstosogame.data.LocationData;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class TosoConfig {

    private final LocationData waitingLocation;

    private LocationData prisonLocation;

    private LocationData openingGameLocation;

    private LocationData goalLocation;

    private final CuboidData goalArea;

    private final CuboidData prisonArea;

    private final Set<LocationData> explosionHunterBoxes;

    private final Set<LocationData> coreLocations;

    private final long gameTime;

    private final long placeBlockMissionTime;

    private final long breakMissionTime;

    private final long craftMissionTime;

    private final long breakFailedTime;

    private final long finishTime;

    private final long flyTime;

    private int lottery;


    public TosoConfig(LocationData waitingLocation) {
        this.waitingLocation = waitingLocation;
        this.prisonLocation = waitingLocation;
        this.openingGameLocation = waitingLocation;
        this.goalLocation = waitingLocation;
        this.explosionHunterBoxes = Set.of(waitingLocation);
        this.coreLocations = Set.of(waitingLocation);
        this.goalArea = new CuboidData(waitingLocation, waitingLocation);
        this.prisonArea = new CuboidData(waitingLocation, waitingLocation);
        this.gameTime = 60;
        this.placeBlockMissionTime = 50;
        this.breakMissionTime = 40;
        this.craftMissionTime = 30;
        this.breakFailedTime = 10;
        this.finishTime = 30;
        this.flyTime = 10;
        this.lottery = 10;
    }

    public TosoConfig(LocationData waitingLocation, LocationData prisonLocation, LocationData openingGameLocation,
                      LocationData goalLocation, Set<LocationData> explosionHunterBoxes, Set<LocationData> coreLocations, LocationData goalAreaPosOne, LocationData goalAreaPosTwo,
                      CuboidData prisonArea, long gameTime, long blockTime, long breakTime, long craftTime, long breakFailedTime, long finishTime, long flyTime, int lottery) {

        this.waitingLocation = waitingLocation;
        this.prisonLocation = prisonLocation;
        this.openingGameLocation = openingGameLocation;
        this.goalLocation = goalLocation;
        this.explosionHunterBoxes = new HashSet<>(explosionHunterBoxes);
        this.coreLocations = new HashSet<>(coreLocations);
        this.goalArea = new CuboidData(goalAreaPosOne, goalAreaPosTwo);
        this.prisonArea = prisonArea;
        this.gameTime = gameTime;
        this.placeBlockMissionTime = gameTime - blockTime;
        this.breakMissionTime = gameTime - breakTime;
        this.craftMissionTime = gameTime - craftTime;
        this.breakFailedTime = breakFailedTime;
        this.finishTime = finishTime;
        this.flyTime = flyTime;
        this.lottery = lottery;
    }

    public void setCoreLocations(LocationData coreLocations) {
        this.coreLocations.add(coreLocations);
    }
}
