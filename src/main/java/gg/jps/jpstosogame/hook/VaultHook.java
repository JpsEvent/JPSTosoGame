package gg.jps.jpstosogame.hook;

import org.bukkit.plugin.java.JavaPlugin;
import pakira.hook.EconomyHook;
import pakira.player.OnlinePlayer;

import java.math.BigDecimal;

public class VaultHook extends EconomyHook {

    protected VaultHook(JavaPlugin plugin, String name) {
        super(plugin, name);
    }

    @Override
    public BigDecimal getBalance(OnlinePlayer player) {
        return null;
    }

    @Override
    public boolean hasMoney(OnlinePlayer player, BigDecimal amount) {
        return false;
    }

    @Override
    public void takeMoney(OnlinePlayer player, BigDecimal amount) {
        player.getPlayer().setLevel(Math.max(0, amount.intValue()));
    }

    @Override
    public void giveMoney(OnlinePlayer player, BigDecimal amount) {
        player.getPlayer().setLevel(Math.max(0, amount.intValue()));
    }

    @Override
    public String formatMoney(BigDecimal amount) {
        return null;
    }

    @Override
    protected void onEnable() {

    }
}
