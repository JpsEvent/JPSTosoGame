package gg.jps.jpstosogame.game;

import gg.jps.jpstosogame.data.CuboidData;
import gg.jps.jpstosogame.data.LocationData;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public final class TosoConfig {

    private LocationData waitingLocation;

    private LocationData prisonLocation;

    private LocationData openingGameLocation;

    private LocationData goalLocation;

    private CuboidData goalArea;

    private Set<LocationData> explosionHunterBoxes;

    private Set<LocationData> coreLocations;

    public TosoConfig(LocationData waitingLocation) {
        this.waitingLocation = waitingLocation;
        this.prisonLocation = waitingLocation;
        this.openingGameLocation = waitingLocation;
        this.goalLocation = waitingLocation;
        this.explosionHunterBoxes = new HashSet<>();
        this.coreLocations = new HashSet<>();
        this.goalArea = new CuboidData(waitingLocation, waitingLocation);
    }

    public void setCoreLocations(LocationData coreLocations) {
        this.coreLocations.add(coreLocations);
    }
}
