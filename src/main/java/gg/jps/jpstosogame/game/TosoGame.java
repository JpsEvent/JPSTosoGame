package gg.jps.jpstosogame.game;

import gg.jps.jpstosogame.JpsTosoGame;
import gg.jps.jpstosogame.game.handler.InGameHandler;
import gg.jps.jpstosogame.game.handler.WaitingHandler;
import gg.jps.jpstosogame.mission.MissionManager;
import gg.jps.jpstosogame.player.GamePlayer;
import gg.jps.jpstosogame.util.Cuboid;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import pakira.game.Game;
import pakira.game.Handler;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Getter
public class TosoGame extends Game {

    private final TosoConfig config;

    private final TosoTime tosoTime;

    private final TosoEscapeTime tosoEscapeTime;

    private final MissionManager missionManager;

    private final WaitingHandler waitingHandler = new WaitingHandler(this);

    private final InGameHandler inGameHandler = new InGameHandler(this);

    private final Cuboid goalArea;

    private final Cuboid prisonArea;

    private final Set<UUID> escapePlayers = new HashSet<>();

    private final Set<UUID> hunterPlayers = new HashSet<>();
    private final Set<UUID> diamondGetPlayers = new HashSet<>();

    public TosoGame(TosoConfig config) {
        super(JpsTosoGame.getInstance());
        this.config = config;
        this.tosoTime = new TosoTime(this);
        this.tosoEscapeTime = new TosoEscapeTime(this);
        this.missionManager = new MissionManager(this);
        this.goalArea = new Cuboid(config.getGoalArea().getPos1().getLocation(), config.getGoalArea().getPos2().getLocation());
        this.prisonArea = new Cuboid(config.getPrisonArea().getPos1().getLocation(), config.getPrisonArea().getPos2().getLocation());

        getCurrentHandler().register(JpsTosoGame.getInstance());
    }

    @Override
    public Handler[] getHandlers() {
        return new Handler[]{
                waitingHandler,
                inGameHandler,
        };
    }

    public void setEscapePlayers() {
        getPlayers().forEach(player -> {
            if (isHunter(player.getPlayer())) return;

            escapePlayers.add(player.getUniqueId());
        });
    }

    public void stopGame() {
        getBossBar().hide();
        tosoTime.cancel();
        tosoEscapeTime.cancel();
        inGameHandler.reset();
        escapePlayers.clear();
        hunterPlayers.clear();
    }

    public void addEscape(GamePlayer player) {
        if (isHunter(player.getPlayer())) return;
        escapePlayers.add(player.getUniqueId());
    }

    public void addDiamondGetPlayer(GamePlayer player){
        if (isHunter(player.getPlayer()))return;
        diamondGetPlayers.add(player.getUniqueId());
    }

    public boolean isGetDiamond(GamePlayer player){
        return diamondGetPlayers.contains(player.getUniqueId());
    }

    public void addHunter(GamePlayer player) {
        hunterPlayers.add(player.getUniqueId());
        player.netherite();
    }

    public void removeHunter(GamePlayer player) {
        hunterPlayers.remove(player.getUniqueId());
        player.unEquip();
    }

    public void sendHunterList(Player player) {
        player.sendMessage(hunterPlayers.stream().map(uuid -> Bukkit.getOfflinePlayer(uuid) == null ? "not find" : Bukkit.getOfflinePlayer(uuid).getName()).collect(Collectors.joining("\n")));
    }

    public void creativeMode() {
        title("&cハンター飛行開始", "");
        sound(Sound.ENTITY_WITHER_SPAWN);
        hunterPlayers.forEach(uuid -> JpsTosoGame.getInstance().getPlayer(uuid).ifPresent(GamePlayer::flyable));
    }

    public void teleportWaitingLocation(GamePlayer player) {
        player.teleport(getConfig().getWaitingLocation().getLocation());
    }

    public void teleportPrisonLocation(GamePlayer player) {
        player.teleport(getConfig().getPrisonLocation().getLocation());
    }

    public void teleportOpeningGameLocation(GamePlayer player) {
        player.teleport(getConfig().getOpeningGameLocation().getLocation());
    }

    public void teleportGoalLocation(GamePlayer player) {
        player.teleport(getConfig().getGoalLocation().getLocation());
    }

    public boolean isInGoalArea(Player player) {
        return goalArea.isIn(player);
    }

    public boolean isInPrisonArea(Player player) {
        return prisonArea.isIn(player);
    }

    public boolean isHunter(Player player) {
        return hunterPlayers.contains(player.getUniqueId());
    }

    public void sendMessageOwnTeam(GamePlayer player, String message) {
        if (hunterPlayers.contains(player.getUniqueId())) {
            hunterPlayers.forEach(uuid -> {
                final Player p = Bukkit.getPlayer(uuid);
                if (p == null) return;

                p.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("[ハンター] %s&r: &7%s", player.getUsername(), message)));
            });
            return;
        }

        if (escapePlayers.contains(player.getUniqueId())) {
            getEscapePlayers().forEach(uuid -> {
                final Player p = Bukkit.getPlayer(uuid);
                if (p == null) return;

                p.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("[逃走者] %s&r: &7%s", player.getUsername(), message)));
            });
        }
    }

    public boolean isSpectator(Player player) {
        return player.getGameMode() == GameMode.SPECTATOR;
    }

    public void sendMessageSpectators(GamePlayer player, String message) {
    }
}
