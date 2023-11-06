package gg.jps.jpstosogame.game;

import gg.jps.jpstosogame.data.CuboidData;
import gg.jps.jpstosogame.data.LocationData;
import gg.jps.jpstosogame.util.Cuboid;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class TosoConfig {

    private LocationData waitingLocation;

    private LocationData prisonLocation;

    private LocationData openingGameLocation;

    private LocationData goalLocation;

    private CuboidData goalArea;

    private Set<LocationData> explosionHunterBoxes;

    private Set<LocationData> coreLocations;

    private long gameTime;

    private long placeBlockMissionTime;

    private long breakMissionTime;

    private long craftMissionTime;

    private long escapeMission;

    private long coolTime;

    private long breakFailedTime;

    private long craftFailedTime;


    /*public TosoConfig(LocationData waitingLocation) {
        this.waitingLocation = waitingLocation;
        this.prisonLocation = waitingLocation;
        this.openingGameLocation = waitingLocation;
        this.goalLocation = waitingLocation;
        this.explosionHunterBoxes = new HashSet<>();
        this.coreLocations = new HashSet<>();
        this.goalArea = new CuboidData(waitingLocation, waitingLocation);
        this.gameTime = 40 * 60;
        this.placeBlockMissionTime = gameTime - (10 * 60);
        this.breakMissionTime = gameTime - (15 * 60);
        this.craftMissionTime = gameTime - (20 * 60);
        this.escapeMission = gameTime - (25 * 60);
    }*/

    public TosoConfig(LocationData waitingLocation, LocationData prisonLocation, LocationData openingGameLocation,
                      LocationData goalLocation, Set<LocationData> explosionHunterBoxes, Set<LocationData> coreLocations, LocationData goalAreaPosOne, LocationData goalAreaPosTwo,
                      long gameTime, long blockTime, long breakTime, long craftTime, long escapeTime, long coolTime, long breakFailedTime, long craftFailedTime) {

        this.waitingLocation = waitingLocation;
        this.prisonLocation = prisonLocation;
        this.openingGameLocation = openingGameLocation;
        this.goalLocation = goalLocation;
        this.explosionHunterBoxes = new HashSet<>(explosionHunterBoxes);
        this.coreLocations = new HashSet<>(coreLocations);
        this.goalArea = new CuboidData(goalAreaPosOne, goalAreaPosTwo);
        this.gameTime = gameTime;
        this.placeBlockMissionTime = gameTime - blockTime;
        this.breakMissionTime = gameTime - breakTime;
        this.craftMissionTime = gameTime - craftTime;
        this.escapeMission = gameTime - escapeTime;
        this.coolTime = coolTime;
        this.breakFailedTime = breakFailedTime;
        this.craftFailedTime = craftFailedTime;
    }

    public void setCoreLocations(LocationData coreLocations) {
        this.coreLocations.add(coreLocations);
    }
}
