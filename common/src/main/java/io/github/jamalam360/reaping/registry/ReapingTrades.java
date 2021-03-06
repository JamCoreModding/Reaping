package io.github.jamalam360.reaping.registry;

import dev.architectury.registry.level.entity.trade.SimpleTrade;
import dev.architectury.registry.level.entity.trade.TradeRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.VillagerProfession;

/**
 * @author Jamalam
 */
public class ReapingTrades {
    public static void registerTrades() {
        ReapingItems.IRON_REAPER.listen((item) -> TradeRegistry.registerVillagerTrade(
                VillagerProfession.BUTCHER,
                2,
                new SimpleTrade(
                        new ItemStack(Items.EMERALD, 3),
                        ItemStack.EMPTY,
                        item.getDefaultStack(),
                        5,
                        10,
                        1
                )
        ));

        ReapingItems.DIAMOND_REAPER.listen((item) -> TradeRegistry.registerVillagerTrade(
                VillagerProfession.BUTCHER,
                5,
                new SimpleTrade(
                        new ItemStack(Items.EMERALD, 13),
                        ItemStack.EMPTY,
                        item.getDefaultStack(),
                        3,
                        30,
                        1
                )
        ));

        ReapingItems.HUMAN_MEAT.listen((item) -> TradeRegistry.registerTradeForWanderingTrader(
                true,
                new SimpleTrade(
                        new ItemStack(Items.EMERALD, 7),
                        ItemStack.EMPTY,
                        item.getDefaultStack(),
                        6,
                        20,
                        1
                )
        ));
    }
}
