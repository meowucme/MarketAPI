package meowucme.market.inventories.markets;

import meowucme.market.MarketItem;
import meowucme.market.actions.CommandAction;
import meowucme.market.actions.MarketAction;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.util.Arrays;

public abstract class Market implements InventoryHolder {
    private final Inventory inv;
    private final float[] buy = new float[54];
    private final float[] sell = new float[54];
    private final MarketItem[] marketItems = new MarketItem[54];
    private final ItemStack[] trueItems = new ItemStack[54];
    private final MarketAction[] actions = new meowucme.market.actions.MarketAction[54];

    public final float getBuyValue(int index) {
        return buy[index];
    }

    public final float getSellValue(int index) {
        return sell[index];
    }

    public final MarketItem getMarketItem(int index) {
        return marketItems[index];
    }

    public final ItemStack getTrueItem(int index) {
        return trueItems[index];
    }

    public final void execute(int index, MarketAction.Context context) {
        actions[index].execute(context);
    }

    protected final void addShopItem(int slot, MarketItem item, MarketAction marketAction) {
        inv.setItem(slot, item);
        buy[slot] = item.buy;
        sell[slot] = item.sell;
        marketItems[slot] = item;
        trueItems[slot] = item.itemStack;
        actions[slot] = marketAction;
    }

    @Override
    public final Inventory getInventory() {
        return inv;
    }

    public Market() {
        Arrays.fill(buy, -1);
        Arrays.fill(sell, -1);

        inv = Bukkit.createInventory(
                this,
                54,
                ChatColor.of(new Color(100, 100, 100)) + "Black Market"
        );

        fillInventory();
    }

    protected abstract void fillInventory();

    public final void init() {

    }
}
