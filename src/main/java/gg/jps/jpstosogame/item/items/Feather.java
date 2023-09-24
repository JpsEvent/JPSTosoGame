package gg.jps.jpstosogame.item.items;

import gg.jps.jpstosogame.item.Item;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Feather extends Item {

    public Feather() {
        super(Material.FEATHER, "俊敏の羽", "俊敏エフェクト10が5秒つく");
    }

    @Override
    public void onClick(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20, 1));
        consume(player);
    }
}
