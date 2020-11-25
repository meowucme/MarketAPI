package meowucme.market;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MarketItem extends ItemStack {
    public final ItemStack itemStack;
    public final float buy, sell;
    public MarketItem(ItemStack itemStack, float buy, float sell) {
        super(itemStack);
        this.itemStack = itemStack;
        this.buy = buy;
        this.sell = sell;
        ItemMeta meta = getItemMeta();
        if(meta != null) {
            List<String> trueLore = meta.getLore();
            List<String> lore = trueLore == null ? new ArrayList<>() : new ArrayList<>(trueLore);

            if(buy != -1) {
                lore.add(ChatColor.AQUA + "Buy: " + ChatColor.RED + "$" + buy);
            }

            if(sell != -1) {
                lore.add(ChatColor.AQUA + "Sell: " + ChatColor.GREEN + "$" + sell);
            }

            meta.setLore(lore);
        }
        setItemMeta(meta);
    }
}
