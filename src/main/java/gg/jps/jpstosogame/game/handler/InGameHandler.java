package gg.jps.jpstosogame.game.handler;

import gg.jps.jpstosogame.JpsTosoGame;
import gg.jps.jpstosogame.game.TosoConfig;
import gg.jps.jpstosogame.game.TosoGame;
import gg.jps.jpstosogame.player.GamePlayer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import pakira.game.Handler;
import pakira.player.OnlinePlayer;
import pakira.util.ItemBuilder;

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
        game.setEscapePlayers();
        game.getPlayers(GamePlayer.class).forEach(GamePlayer::unFreeze);
    }

    @Override
    public void end() {
        title("&5ゲーム終了", "");
        sound(Sound.ENTITY_WITHER_DEATH);
        getPlayers(GamePlayer.class).forEach(GamePlayer::glow);
        showResult();
        nextHandler();
        reset();
    }

    @Override
    public void reset() {
        escapedPlayers.clear();
        kills.clear();
    }

    @Override
    public void join(OnlinePlayer player) {
        getGame().getBossBar().addPlayer(player);
    }

    @EventHandler
    private void onAxe(PlayerInteractEvent event) {
        final ItemStack item = event.getItem();
        final GamePlayer player = JpsTosoGame.getInstance().getPlayer(event.getPlayer());

        if (event.getHand() != EquipmentSlot.HAND) return;
        if (item == null) return;

        if (item.isSimilar(ItemBuilder.of(Material.GOLDEN_AXE).build())) {
            game.getPlayers(GamePlayer.class).forEach(GamePlayer::unFreeze);
            game.getMissionManager().startEscapeMission();
        }
    }

    @EventHandler
    private void onMove(PlayerMoveEvent event) {
        if (game.isInGoalArea(event.getPlayer())) {
            game.getInGameHandler().goal(event.getPlayer());
        }
    }

    private void goal(Player player) {
        if (game.isHunter(player)) return;
        if (escapedPlayers.contains(player.getUniqueId())) return;

        broadcast(String.format("%sが脱出に成功しました。", player.getName()));
        sound(Sound.UI_TOAST_CHALLENGE_COMPLETE);
        player.teleport(game.getConfig().getGoalLocation().getLocation());
        player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
        escapedPlayers.add(player.getUniqueId());
    }

    @EventHandler
    private void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            if (event.getEntity() instanceof Player vitim) {
                if (game.isHunter(player) != game.isHunter(vitim)) {
                    if (game.isHunter(player) && player.getInventory().getItemInMainHand().getType() == Material.NETHERITE_SWORD) {
                        event.setDamage(100);
                        return;
                    }
                    return;
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onTeamChat(AsyncPlayerChatEvent event) {

        event.setCancelled(true);
        final GamePlayer player = JpsTosoGame.getInstance().getPlayer(event.getPlayer());
        final String message = event.getMessage();
        if (game.isSpectator(player.getPlayer())) {
            game.sendMessageSpectators(player, message);
            return;
        }
            /*if (event.getMessage().startsWith("@")) {
                game.shout(sender, message.substring(1));
                return;
            }*/
        game.sendMessageOwnTeam(player, message);
    }

    @EventHandler
    private void onBlockClick(PlayerInteractEvent event) {
        final Block clickedBlock = event.getClickedBlock();
        final GamePlayer player = JpsTosoGame.getInstance().getPlayer(event.getPlayer());

        if (clickedBlock == null || event.getHand() != EquipmentSlot.HAND) return;

        if (clickedBlock.getType() == Material.REDSTONE_BLOCK) {
            getGame().teleportPrisonLocation(player);
            getGame().addEscape(player);
        }
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
