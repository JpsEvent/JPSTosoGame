package gg.jps.jpstosogame.item.items;

import gg.jps.jpstosogame.item.Item;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.ProjectileHitEvent;

public class KnockBall extends Item {

    public KnockBall() {
        super(Material.SNOWBALL, "ノックボール", "当たると10マス飛ぶ雪玉を投げる");
    }

    public void onProjectileHit(ProjectileHitEvent event) {
        final Projectile projectile = event.getEntity();

        if (projectile instanceof Snowball) {
            if (event.getHitEntity() == null) return;
            if (event.getHitEntity().getType() == EntityType.PLAYER) {
                Player player = (Player) event.getHitEntity();

                if (player.getGameMode() != GameMode.CREATIVE) {
                    player.setVelocity(projectile.getVelocity().multiply(5));
                }
            }
        }
    }
}
