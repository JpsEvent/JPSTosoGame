package pakira.game;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import pakira.player.OnlinePlayer;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class Handler implements Listener {

    private final Game game;

    protected Handler(Game game) {
        this.game = game;
    }

    public final void register(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public final void unregister() {
        HandlerList.unregisterAll(this);
    }

    public void join(OnlinePlayer player) {

    }

    public void leave(OnlinePlayer player) {

    }

    public void start() {

    }

    public void end() {

    }

    public void reset() {

    }

    public final void nextHandler() {
        game.nextHandler();
    }

    public final void nextHandlerAndStart() {
        game.nextHandlerAndStart();
    }

    public void title(String title, String subTitle) {
        getGame().title(title, subTitle);
    }

    public final void broadcast(String msg) {
        game.broadcast(msg);
    }

    public final void broadcast(Component msg) {
        game.broadcast(msg);
    }

    public final void sound(Sound sound) {
        game.sound(sound);
    }

    public Game getGame() {
        return game;
    }


    public <T extends OnlinePlayer> List<T> getPlayers(Class<T> type) {
        return getGame().getPlayers().stream().map(type::cast).collect(Collectors.toList());
    }

    public Set<OnlinePlayer> getPlayers() {
        return getGame().getPlayers();
    }

}
