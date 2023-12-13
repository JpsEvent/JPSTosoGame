package gg.jps.jpstosogame.item.items;

import gg.jps.jpstosogame.item.Item;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GhastTear extends Item {


    public GhastTear() {
        super(Material.GHAST_TEAR, "ガストの涙", "自分と自分の周り５マスのプレイヤーが透明化＋俊敏レベル10がつく");
    }

    @Override
    public void onClick(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        addPotionEffect(player);
        player.getNearbyEntities(2.5,2.5,2.5).forEach(entity -> {
            if (entity instanceof Player other) {
                addPotionEffect(other);
            }
        });
        consume(player);
    }

    private void addPotionEffect(Player player) {
        // 5秒間
        final int duration = 5 * 20;

        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, duration, 10));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, 10));
    }
}
