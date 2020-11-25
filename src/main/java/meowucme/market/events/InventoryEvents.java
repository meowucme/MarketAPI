package meowucme.market.events;

import meowucme.market.actions.CommandAction;
import meowucme.market.actions.MarketAction;
import meowucme.market.inventories.markets.Market;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import static meowucme.market.Market.eco;

public class InventoryEvents implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event != null) {
            return;
        }

        Inventory clicked = event.getInventory();
        InventoryHolder holder = clicked.getHolder();
        if(holder instanceof Market) {
            event.setCancelled(true);

            Inventory inv = event.getClickedInventory();
            if(inv != clicked) {
                return;
            }

            Market market = (Market)holder;
            int slot = event.getSlot();
            float cost = market.getBuyValue(slot);
            float sell = market.getSellValue(slot);
            Player player = (Player)event.getWhoClicked();

            World world = player.getWorld();
            Location location = player.getLocation();

            ItemStack currentItem = event.getCurrentItem();
            ItemStack trueItem = market.getTrueItem(slot);

            if(currentItem == null) {
                return;
            }

            boolean free = player.hasPermission("meowucme.market.free");

            ClickType clickType = event.getClick();

            if(clickType == ClickType.LEFT) {
                if(cost == -1 && !player.hasPermission("meowucme.market.buy_any")) {
                    player.sendMessage(ChatColor.RED + "This item cannot be purchased.");
                    player.playSound(location, Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.AMBIENT, 1, 1);
                    market.execute(slot, new MarketAction.Context(MarketAction.MarketEvent.buy, MarketAction.SuccessState.not_allowed, player, market, slot));
                } else {
                    if (eco.getBalance(player) >= cost || free) {
                        if (!free) {
                            eco.withdrawPlayer(player, cost);
                        }

                        player.sendMessage(ChatColor.GREEN + "Purchased " + currentItem.getI18NDisplayName() + ChatColor.RESET + (ChatColor.GREEN + " for $" + cost));
                        player.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, SoundCategory.AMBIENT, 1, 2);

                        for (ItemStack itemStack : player.getInventory().addItem(trueItem).values()) {
                            world.dropItem(location, itemStack);
                        }

                        market.execute(slot, new MarketAction.Context(MarketAction.MarketEvent.buy, MarketAction.SuccessState.success, player, market, slot));
                    } else {
                        player.sendMessage(ChatColor.RED + "You do not have the required funds.");
                        player.playSound(location, Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.AMBIENT, 1, 1);
                        market.execute(slot, new MarketAction.Context(MarketAction.MarketEvent.buy, MarketAction.SuccessState.fail, player, market, slot));
                    }
                }
            } else if(clickType == ClickType.RIGHT) {
                if(sell == -1) {
                    player.sendMessage(ChatColor.RED + "This item cannot be sold.");
                    player.playSound(location, Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.AMBIENT, 1, 1);
                    market.execute(slot, new MarketAction.Context(MarketAction.MarketEvent.sell, MarketAction.SuccessState.not_allowed, player, market, slot));
                } else {
                    for(ItemStack item : player.getInventory().getContents()) {
                        /*if(
                                trueItem.getType() == item.getType() &&
                                trueItem.getAmount() >= item.getAmount() &&
                                trueItem.getItemMeta().equals(item.getItemMeta())
                        ) */{
                            player.getInventory().remove(currentItem);
                            eco.depositPlayer(player, sell);
                            player.sendMessage(ChatColor.GREEN + "Sold " + currentItem.getI18NDisplayName() + ChatColor.RESET + (ChatColor.GREEN + " for $" + sell));
                            market.execute(slot, new MarketAction.Context(MarketAction.MarketEvent.sell, MarketAction.SuccessState.success, player, market, slot));
                            player.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, SoundCategory.AMBIENT, 1, 2);
                            return;
                        }
                    }

                    player.sendMessage(ChatColor.RED + "You do not have enough of this item to sell.");
                    player.playSound(location, Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.AMBIENT, 1, 1);
                    market.execute(slot, new MarketAction.Context(MarketAction.MarketEvent.sell, MarketAction.SuccessState.fail, player, market, slot));
                }
            }
        }
    }
}
