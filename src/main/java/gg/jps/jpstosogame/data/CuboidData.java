package gg.jps.jpstosogame.data;

import lombok.Getter;

@Getter
public class CuboidData {

    private final LocationData pos1;
    private final LocationData pos2;

    public CuboidData(LocationData pos1, LocationData pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
    }
}
