package meowucme.market.actions;

import meowucme.market.MarketItem;
import meowucme.market.inventories.markets.Market;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.Serializable;

public interface MarketAction {
    MarketAction none = context -> {};

    void execute(Context context);

    class Context {
        public final MarketEvent event;
        public final Player player;
        public final Market market;
        public final int slot;

        public Context(MarketEvent event, SuccessState state, Player player, Market market, int slot) {
            this.event = event;
            this.player = player;
            this.market = market;
            this.slot = slot;
        }
    }

    enum MarketEvent {
        buy, sell;
    }

    enum SuccessState {
        success, fail, not_allowed;
    }
}
