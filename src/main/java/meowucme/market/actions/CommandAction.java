package meowucme.market.actions;

import org.bukkit.Bukkit;

public class CommandAction implements MarketAction {
    private final String command;

    public CommandAction(String command) {
        this.command = command;
    }

    @Override
    public void execute(Context context) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command
                .replace("%player%", context.player.getName())
        );
    }
}
