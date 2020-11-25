package meowucme.market;

import meowucme.market.events.InventoryEvents;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public final class Market extends JavaPlugin {
    public static Economy eco;

    private static List<JavaPlugin> disable = new ArrayList<>();
    private static boolean hasChecked = false;

    public static void disableUnlessEconomy(JavaPlugin plugin) {
        if(hasChecked) {
            if(eco == null) {
                Bukkit.getPluginManager().disablePlugin(plugin);
            }
        } else {
            disable.add(plugin);
        }
    }

    @Override
    public void onEnable() {
        if(!setupEconomy()) {
            hasChecked = true;

            for(JavaPlugin plugin : disable) {
                getServer().getPluginManager().disablePlugin(plugin);
            }

            Bukkit.broadcast(new TextComponent(ChatColor.of(new Color(200, 0, 0)) + "No economy plugin :("));
            getServer().getPluginManager().disablePlugin(this);
            disable = null;
            return;
        }

        getServer().getPluginManager().registerEvents(new InventoryEvents(), this);

        getServer().getPluginManager().addPermission(new Permission("meowucme.market.free"));
        getServer().getPluginManager().addPermission(new Permission("meowucme.market.buy_any"));
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economy = getServer().getServicesManager().getRegistration(Economy.class);

        if(economy == null) {
            return false;
        }

        eco = economy.getProvider();
        return true;
    }
}
