package gg.jps.jpstosogame.player;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pakira.player.OnlinePlayer;
import pakira.util.ItemBuilder;

public class GamePlayer extends OnlinePlayer {

    private final Player player;

    private boolean isFreeze = false;

    private final ItemStack[] ARMOR_CONTENTS = new ItemStack[]{
            ItemBuilder.of(Material.NETHERITE_BOOTS).enchantment(Enchantment.BINDING_CURSE, 1).build(),
            ItemBuilder.of(Material.NETHERITE_LEGGINGS).enchantment(Enchantment.BINDING_CURSE, 1).build(),
            ItemBuilder.of(Material.NETHERITE_CHESTPLATE).enchantment(Enchantment.BINDING_CURSE, 1).build(),
            ItemBuilder.of(Material.NETHERITE_HELMET).enchantment(Enchantment.BINDING_CURSE, 1).build()
    };

    public GamePlayer(Player player) {
        super(player.getUniqueId(), player.getName());
        this.player = player;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    public void glow() {
        getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 60 * 60, 1));
    }

    public void freeze() {
        if (player.getAllowFlight()) {
            player.setFlying(true);
        }
        player.setWalkSpeed(0F);
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE,237));
        isFreeze = true;
    }

    public void unFreeze() {
        if (player.getAllowFlight()) {
            player.setFlying(false);
        }
        player.setWalkSpeed(0.2F);
        player.removePotionEffect(PotionEffectType.JUMP);
        isFreeze = false;
    }

    public void firework() {
        player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
    }

    public void netherite() {
        final PlayerInventory inventory = player.getInventory();

        inventory.setArmorContents(ARMOR_CONTENTS);
        inventory.addItem(ItemBuilder.of(Material.NETHERITE_SWORD).build());
    }

    public boolean isFreeze() {
        return isFreeze;
    }

    public void strong() {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000,1));
    }

    public void flyable() {
        player.setAllowFlight(true);
    }

    public void unEquip() {
        final PlayerInventory inventory = player.getInventory();
        inventory.clear();
    }
}
