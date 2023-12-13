package gg.jps.jpstosogame.item;

import com.google.common.collect.ImmutableMap;
import gg.jps.jpstosogame.JpsTosoGame;
import gg.jps.jpstosogame.item.items.Feather;
import gg.jps.jpstosogame.item.items.GhastTear;
import gg.jps.jpstosogame.item.items.KnockBall;
import gg.jps.jpstosogame.item.items.Sword;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Map;

public class ItemManager implements Listener {

    private final Map<Material, Item> ITEMS;

    public ItemManager() {
        Bukkit.getPluginManager().registerEvents(this, JpsTosoGame.getInstance());

        ITEMS = ImmutableMap.of(
                Material.FEATHER, new Feather(),
                Material.GHAST_TEAR, new GhastTear(),
                Material.SNOWBALL, new KnockBall(),
                Material.NETHERITE_SWORD, new Sword()
        );
    }

    @EventHandler
    private void on(PlayerInteractEvent event) {
        final Action action = event.getAction();

        //アイテムを持った状態でのメインハンドによるクリックでなければ戻る
        if (action == Action.PHYSICAL || event.getHand() != EquipmentSlot.HAND || !event.hasItem()) return;

        //クリックしたプレイヤーを取得する
        final Player player = event.getPlayer();

        Material material = event.getMaterial();
        //対応したアイテムが存在しなければ戻る
        if (!ITEMS.containsKey(material)) return;

        ITEMS.forEach((mate, item) -> {
            if (material != mate) return;

            item.onClick(event);
        });

        event.setCancelled(true);
    }

    @EventHandler
    private void onHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Snowball snowball) {
            if (snowball.getShooter() instanceof Player) {
                ITEMS.get(Material.SNOWBALL).onProjectileHit(event);
            }
        }
    }
}
