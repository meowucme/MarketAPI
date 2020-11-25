package meowucme.market.commands;

import meowucme.market.inventories.markets.Market;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

public class MarketCommand implements TabExecutor {
    private final Market market;
    private final String otherPermission;

    public MarketCommand(Market market, String otherPermission) {
        this.market = market;
        this.otherPermission = otherPermission;
    }

    public void register(String commandName) {
        Bukkit.getPluginManager().addPermission(new Permission(otherPermission){{setDefault(PermissionDefault.OP);}});

        PluginCommand command = Bukkit.getPluginCommand(commandName);

        command.setExecutor(this);
        command.setTabCompleter(this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length > 1) {
            return false;
        }

        if(args.length == 0) {
            if(sender instanceof Player) {
                sender.sendMessage("Opening Black Market...");
                ((Player) sender).openInventory(market.getInventory());
            } else {
                sender.sendMessage("You are not a player.");
            }
        } else {
            if(!sender.hasPermission(otherPermission)) {
                sender.sendMessage("You do not have permission.");
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if(target == null) {
                sender.sendMessage("Unable to find player");
            } else {
                sender.sendMessage("Opening Black Market for " + args[0] + "...");
                target.sendMessage("Opening Black Market...");
                target.openInventory(market.getInventory());
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length == 1) {
            return null;
        }

        return new ArrayList<>();
    }
}
