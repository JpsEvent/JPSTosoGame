package gg.jps.jpstosogame.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import gg.jps.jpstosogame.JpsTosoGame;
import gg.jps.jpstosogame.data.LocationData;
import gg.jps.jpstosogame.game.TosoConfig;
import gg.jps.jpstosogame.game.handler.InGameHandler;
import gg.jps.jpstosogame.game.handler.WaitingHandler;
import org.bukkit.entity.Player;
import pakira.game.Game;

import java.util.Locale;

@CommandAlias("jtg")
public class SetCommand extends BaseCommand {

    @Subcommand("start")
    @CommandPermission("toso.start")
    public void on(){
        JpsTosoGame.getInstance().getGame().ifPresent(tosoGame -> {
            if (tosoGame.getCurrentHandler() instanceof WaitingHandler waitingHandler){
                waitingHandler.nextHandlerAndStart();
            }
        });
    }

    @Subcommand("set")
    @CommandPermission("toso.set")
    public void on(Player player, Option option) {
        JpsTosoGame.getInstance().getGame().ifPresent(game -> {
            final TosoConfig config = game.getConfig();
            final LocationData locationData = LocationData.at(player.getLocation());

            switch (option) {
                case SET_OPENING_GAME_LOCATION -> config.setOpeningGameLocation(locationData);
                case SET_PRISON_LOCATION -> config.setPrisonLocation(locationData);
                // case SET_CORE_LOC -> config.setCoreLocations(locationData);
                case SET_LAVA_LOCATION -> config.setLavaLocation(locationData);
                case SET_DIAMOND_BLOCK -> config.setDiamondBlockLocation(locationData);
                case SET_LAVA_LEVER -> config.setLavaStopLever(locationData);
                case SET_GOAL_LOCATION -> config.setGoalLocation(locationData);
            }

            player.sendMessage(String.format(option.name().toLowerCase(Locale.ROOT) + "を%sに設定しました",
                    String.format("x: %s y: %s z: %s",
                            locationData.getX(),
                            locationData.getY(),
                            locationData.getZ()
                    ))
            );
        });
    }

    @Subcommand("set lottery")
    @CommandPermission("toso.set.lottery")
    public void on(Player player, int lottery) {
        JpsTosoGame.getInstance().getGame().ifPresent(game -> {
            final TosoConfig config = game.getConfig();

            config.setLottery(lottery);

            player.sendMessage(String.format("lotteryを%sに設定しました", lottery)
            );
        });
    }

    @Subcommand("reload")
    @CommandPermission("toso.reload")
    public void on(Player player) {
        JpsTosoGame.getInstance().reload();
        player.sendMessage("設定を読み込みました");
    }

    ///jtg hunter <playerID>
    @Subcommand("hunter")
    @CommandPermission("toso.hunter")
    public void on(Player player, HunterOption option, @Optional OnlinePlayer onlinePlayer) {
        JpsTosoGame.getInstance().getGame().ifPresent(game -> {


            switch (option) {
                case ADD -> {
                    if (onlinePlayer == null) return;
                    game.addHunter(JpsTosoGame.getInstance().getPlayer(onlinePlayer.getPlayer()));
                }
                case REMOVE -> {
                    if (onlinePlayer == null) return;
                    game.removeHunter(JpsTosoGame.getInstance().getPlayer(onlinePlayer.getPlayer()));
                }
                case LIST -> game.sendHunterList(player);
            }
        });
    }

    private enum Option {
        SET_OPENING_GAME_LOCATION,
        SET_PRISON_LOCATION,
        // SET_CORE_LOC,
        SET_LAVA_LEVER,
        SET_LAVA_LOCATION,
        SET_DIAMOND_BLOCK,
        SET_GOAL_LOCATION
    }

    private enum HunterOption {
        ADD,
        REMOVE,
        LIST
    }
}