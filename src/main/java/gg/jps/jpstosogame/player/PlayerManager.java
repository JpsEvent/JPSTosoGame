package gg.jps.jpstosogame.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class PlayerManager implements Listener {

    private final HashMap<UUID, GamePlayer> players = new HashMap<>();

    public PlayerManager(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public GamePlayer getPlayer(Player player) {
        final UUID uuid = player.getUniqueId();
        if (players.containsKey(uuid)) {
            GamePlayer gamePlayer = players.get(uuid);

            if (gamePlayer.getPlayer() != player) {
                gamePlayer = new GamePlayer(player);
                players.put(uuid, gamePlayer);

                return gamePlayer;
            }

            return gamePlayer;
        }

        final GamePlayer gamePlayer = new GamePlayer(player);
        players.put(uuid, gamePlayer);

        return gamePlayer;
    }
}
