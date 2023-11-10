package gg.jps.jpstosogame;

import co.aikar.commands.PaperCommandManager;
import gg.jps.jpstosogame.command.SetCommand;
import gg.jps.jpstosogame.data.CuboidData;
import gg.jps.jpstosogame.data.LocationData;
import gg.jps.jpstosogame.game.TosoConfig;
import gg.jps.jpstosogame.game.TosoGame;
import gg.jps.jpstosogame.item.ItemManager;
import gg.jps.jpstosogame.listener.EventListener;
import gg.jps.jpstosogame.player.GamePlayer;
import gg.jps.jpstosogame.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import pakira.player.OnlinePlayer;
import pakira.scheduler.Scheduler;
import pakira.util.ItemBuilder;
import pakira.util.JsonUtil;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public final class JpsTosoGame extends JavaPlugin {

    private static JpsTosoGame instance;

    public static JpsTosoGame getInstance() {
        return instance;
    }

    private TosoGame game;

    private PlayerManager playerManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        playerManager = new PlayerManager(this);

        loadGame();

        new ItemManager();

        registerRecipe();
        registerCommands();

        joinGameOnlinePlayers();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        if (game != null) {
            game.stopGame();

            try {
                JsonUtil.saveJsonFile(new File(getDataFolder(), "config.json"), game.getConfig());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        leaveGameOnlinePlayers();
    }

    private void loadGame() {
        Optional<TosoConfig> tosoConfig = JsonUtil.loadFromJsonFile(new File(getDataFolder(), "config.json"), TosoConfig.class);

        tosoConfig.ifPresentOrElse(config -> {
            game = new TosoGame(config);
            new EventListener(game);
        }, () -> {
            // generate config.yml, if not exist config.yml
            final LocationData locationData = LocationData.at(Bukkit.getWorlds().get(0).getSpawnLocation());
            try {
                final TosoConfig config = new TosoConfig(locationData);
                JsonUtil.saveJsonFile(new File(getDataFolder(), "config.json"), config);
                loadGame();
            } catch (IOException e) {
                // disable plugin
                onDisable();
                Bukkit.getPluginManager().disablePlugin(this);
                throw new RuntimeException(e);
            }
        });
    }


    private void registerRecipe() {
        final NamespacedKey namespacedKey = NamespacedKey.minecraft("wand_shapeless");
        if (Bukkit.getServer().getRecipe(namespacedKey) != null) return;
        final ItemStack item = ItemBuilder.of(Material.TRIPWIRE_HOOK).name("&e脱出のカギ").build();
        final ShapelessRecipe recipe = new ShapelessRecipe(namespacedKey, item);

        recipe.addIngredient(Material.RED_WOOL);
        recipe.addIngredient(Material.BLUE_WOOL);

        Bukkit.getServer().addRecipe(recipe);
    }

    private void registerCommands() {
        PaperCommandManager commandManager = new PaperCommandManager(this);

        commandManager.enableUnstableAPI("brigadier");
        commandManager.registerCommand(new SetCommand());
    }

    private void joinGameOnlinePlayers() {
        Bukkit.getOnlinePlayers().forEach(player -> getGame().ifPresent(tosoGame -> tosoGame.join(getPlayer(player))));
    }

    private void leaveGameOnlinePlayers() {
        Bukkit.getOnlinePlayers().forEach(player -> getGame().ifPresent(tosoGame -> tosoGame.leave(getPlayer(player))));
    }

    @Nonnull
    private Scheduler scheduler(boolean async) {
        return new Scheduler(this, async);
    }

    @Nonnull
    public Scheduler asyncScheduler() {
        return scheduler(true);
    }

    @Nonnull
    public Scheduler syncScheduler() {
        return scheduler(false);
    }


    public Optional<TosoGame> getGame() {
        if (game == null) return Optional.empty();

        return Optional.of(game);
    }

    public Optional<GamePlayer> getPlayer(UUID uniqueId) {
        final Player player = Bukkit.getPlayer(uniqueId);
        if (player == null) return Optional.empty();

        return Optional.of(getPlayer(player));
    }

    public GamePlayer getPlayer(Player player) {
        return playerManager.getPlayer(player);
    }

    public void reload() {
        loadGame();
    }
}
