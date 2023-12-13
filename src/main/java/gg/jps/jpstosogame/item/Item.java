package gg.jps.jpstosogame.item;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import pakira.util.ItemBuilder;

public abstract class Item {

    private final Material material;
    private final String name;
    private final String description;

    protected Item(Material material, String name, String description) {
        this.material = material;
        this.name = name;
        this.description = description;
    }

    public void onClick(PlayerInteractEvent event) {

    }

    public void onProjectileHit(ProjectileHitEvent event) {

    }

    public void consume(Player player) {
        final PlayerInventory inventory = player.getInventory();
        final ItemStack item = inventory.getItemInMainHand();

        if (item == null) return;
        final ItemStack decreasedAmountItem = ItemBuilder.of(Material.SLIME_BALL, item.getAmount() - 1).build();
        final int heldItemSlot = inventory.getHeldItemSlot();

        inventory.setItem(heldItemSlot, decreasedAmountItem);
        player.updateInventory();
    }
    public ItemStack getItem() {
        return ItemBuilder.of(material).name(name).lore(description).build();
    }
}
