package gg.jps.jpstosogame.item.items;

import gg.jps.jpstosogame.JpsTosoGame;
import gg.jps.jpstosogame.item.Item;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class Sword extends Item {

    public Sword() {
        super(Material.NETHERITE_SWORD, "退魔の剣", "");
    }

    @Override
    public void onClick(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Location location = player.getLocation();
        final Action action = event.getAction();
        final ItemStack sword = event.getItem();
        assert sword != null;
        if (JpsTosoGame.getInstance().getGame().isEmpty()) return;
        if (player.getCooldown(Material.NETHERITE_SWORD) != 0) return;
        if (sword.getType().equals(Material.NETHERITE_SWORD) && action.isRightClick()) {
            final Vector vector = location.getDirection().normalize().multiply(.65);
            final int maxRange = 20;
            final double maxHitRange = .5;
            final ArmorStand stand = player.getWorld().spawn(location, ArmorStand.class, armorStand -> {
                armorStand.setVisible(false);
                armorStand.setInvulnerable(true);
                armorStand.setSilent(true);
                armorStand.setCollidable(false);
                armorStand.setGravity(false);
                armorStand.setRemoveWhenFarAway(true);
                armorStand.setMarker(true);
                armorStand.setItem(EquipmentSlot.HAND, new ItemStack(Material.NETHERITE_SWORD));
                armorStand.setRightArmPose(new EulerAngle(Math.toRadians(350.0), Math.toRadians(location.getPitch() * -1.0), Math.toRadians(90.0)));
            });

            player.setCooldown(Material.NETHERITE_SWORD, 30 * 20);

            JpsTosoGame.getInstance().syncScheduler().every(1).run(schedulerRunnable -> {
                stand.teleport(stand.getLocation().add(vector));
                stand.getNearbyEntities(maxHitRange, maxHitRange, maxHitRange).forEach(entity -> {
                    if (entity instanceof Player victim) {
                        if (victim == player) return;
                        if (JpsTosoGame.getInstance().getGame().get().isHunter(player)) return;
                        victim.setHealth(0);
                        schedulerRunnable.cancel();
                        stand.remove();
                    }
                });
                if(location.distance(stand.getLocation()) > maxRange || stand.getLocation().getBlock().getRelative(BlockFace.UP).getType().isSolid()) {
                    schedulerRunnable.cancel();
                    stand.remove();
                }
            });
        }
    }
}
