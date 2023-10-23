package gg.jps.jpstosogame.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import gg.jps.jpstosogame.JpsTosoGame;
import gg.jps.jpstosogame.data.LocationData;
import gg.jps.jpstosogame.game.TosoConfig;
import org.bukkit.entity.Player;
import pakira.player.OnlinePlayer;

import java.util.Locale;

@CommandAlias("jtg")
public class SetCommand extends BaseCommand {

    @Subcommand("set")
    @CommandPermission("toso.set")
    public void on(Player player, Option option) {
        JpsTosoGame.getInstance().getGame().ifPresent(game -> {
            final TosoConfig config = game.getConfig();
            final LocationData locationData = LocationData.at(player.getLocation());

            switch (option) {
                case SET_OPENING_GAME_LOCATION -> config.setOpeningGameLocation(locationData);
                case SET_PRISON_LOCATION -> config.setPrisonLocation(locationData);
                case SET_CORE_LOC -> config.setCoreLocations(locationData);
                case SET_GOAL_LOCATION -> config.setGoalLocation(locationData);
            }

            player.sendMessage(option.name().toLowerCase(Locale.ROOT) + "を設定しました");
        });
    }

    ///jtg hunter <playerID>
    @Subcommand("hunter add")
    @CommandPermission("toso.hunter")
    public void on(Player player, HunterOption option, @Optional OnlinePlayer onlinePlayer) {
        JpsTosoGame.getInstance().getGame().ifPresent(game -> {
            final Player target = onlinePlayer.getPlayer();

            switch (option) {
                case ADD -> {
                    if (target == null) return;
                    game.addHunter(JpsTosoGame.getInstance().getPlayer(target));
                }
                case REMOVE -> game.removeHunter(JpsTosoGame.getInstance().getPlayer(target));
                case LIST -> game.sendHunterList(player);
            }
        });
    }

    private enum Option {
        SET_OPENING_GAME_LOCATION,
        SET_PRISON_LOCATION,
        SET_CORE_LOC,
        SET_GOAL_LOCATION
    }

    private enum HunterOption {
        ADD,
        REMOVE,
        LIST
    }
}