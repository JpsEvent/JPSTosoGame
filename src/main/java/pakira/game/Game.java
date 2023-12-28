package pakira.game;


import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;
import pakira.bossbar.GameBossBar;
import pakira.player.OnlinePlayer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class Game {

    private final Set<OnlinePlayer> players = new HashSet<>();
    private final GameBossBar bossBar = new GameBossBar();
    private final JavaPlugin plugin;
    private int currentHandler = 0;

    public Game(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public abstract Handler[] getHandlers();

    public final void join(OnlinePlayer player) {
        players.add(player);
        bossBar.addPlayer(player);
        getCurrentHandler().join(player);
    }

    public final void leave(OnlinePlayer player) {
        players.remove(player);
        bossBar.removePlayer(player);
        getCurrentHandler().leave(player);
    }

    public final void start() {
        getCurrentHandler().start();
    }

    public final void end() {
        bossBar.hide();
        getCurrentHandler().end();
    }

    public final void reset() {
        getCurrentHandler().reset();
    }

    public void title(String title, String subTitle) {
        getPlayers().forEach(player -> player.sendTitle(title, subTitle));
    }

    public final void broadcast(String message) {
        final String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
        getPlayers().forEach(player -> player.sendMessage(Component.text(coloredMessage)));
    }


    public final void broadcast(Component message) {
        getPlayers().forEach(player -> player.sendMessage(message));
    }

    public final void sound(Sound sound) {
        getPlayers().forEach(player -> player.playSound(sound));
    }

    public final void nextHandler() {
        getCurrentHandler().unregister();
        currentHandler = getHandlers().length > currentHandler + 1 ? currentHandler + 1 : 0;
        getCurrentHandler().register(plugin);
    }

    public void nextHandlerAndStart() {
        nextHandler();
        start();
    }

    public final Handler getCurrentHandler() {
        return getHandlers()[currentHandler];
    }

    public <T extends OnlinePlayer> List<T> getPlayers(Class<T> type) {
        return players.stream().map(type::cast).collect(Collectors.toList());
    }

    public Set<OnlinePlayer> getPlayers() {
        return players;
    }

    public GameBossBar getBossBar() {
        return bossBar;
    }
}
