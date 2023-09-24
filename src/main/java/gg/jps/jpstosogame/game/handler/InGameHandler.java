package gg.jps.jpstosogame.game.handler;

import gg.jps.jpstosogame.JpsTosoGame;
import gg.jps.jpstosogame.game.TosoGame;
import gg.jps.jpstosogame.player.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import pakira.game.Handler;

import java.util.*;

public class InGameHandler extends Handler {

    private final Map<UUID, Long> kills = new HashMap<>();
    private final Set<UUID> escapedPlayers = new HashSet<>();

    private final TosoGame game;

    public InGameHandler(TosoGame game) {
        super(game);
        this.game = game;
    }

    @Override
    public TosoGame getGame() {
        return game;
    }


    @Override
    public void start() {
        title("&5逃走中スタート", "");
        sound(Sound.ENTITY_WITHER_SPAWN);
        getGame().getTosoTime().start();
    }

    @Override
    public void end() {
        title("&5ゲーム終了", "");
        sound(Sound.ENTITY_WITHER_DEATH);
        getPlayers(GamePlayer.class).forEach(GamePlayer::glow);
        showResult();
        nextHandler();
    }

    @EventHandler
    public void on(PlayerDeathEvent event) {
        final Player player = event.getPlayer();
        if (getGame().isHunter(event.getEntity())) return;

        respawn(player);

        if (player.getKiller() == null) return;

        addKill(player.getKiller());
        player.getWorld().strikeLightningEffect(player.getLocation());
    }

    private void respawn(Player player) {
        player.setBedSpawnLocation(getGame().getConfig().getPrisonLocation().getLocation(), true);
        JpsTosoGame.getInstance().syncScheduler().run(() -> player.spigot().respawn());
    }

    private void addKill(Player player) {
        final UUID uuid = player.getUniqueId();

        kills.put(uuid, kills.getOrDefault(uuid,0L) + 1);
    }

    private void allPlayerGlow() {
        getPlayers(GamePlayer.class).forEach(GamePlayer::glow);
    }

    private void showResult() {
        showEscapedPlayers();
        showKills();
    }

    private void showEscapedPlayers() {
        broadcast("今回脱出できたメンバー:");

        escapedPlayers.forEach(uuid -> {
            final OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            if (player == null) return;

            broadcast(String.format("  ・ %s", player));
        });
    }

    private void showKills() {
        broadcast("&cハンター確保数:");

        kills.forEach((uuid, kills) -> {
            final OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            if (player == null) return;

            broadcast(String.format("&c%s:%d体", player, kills));
        });
    }
}
