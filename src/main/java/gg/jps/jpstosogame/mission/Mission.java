package gg.jps.jpstosogame.mission;

import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;

@Getter
public abstract class Mission implements Listener {


    private final String title;

    private final Sound sound;


    protected Mission(String title, Sound sound) {
        this.title = title;
        this.sound = sound;
    }

    public void start() {

    }

    public void onFailed() {

    }

    public void onPlace(BlockPlaceEvent event) {

    }

    public void onBreak(BlockBreakEvent event) {

    }

    public void onMove(PlayerMoveEvent event) {

    }
}
