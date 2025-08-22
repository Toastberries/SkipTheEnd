package xyz.toastberries.skiptheend;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class SkipTheEnd implements ModInitializer {
    public static final GameRules.Key<GameRules.BooleanRule> SKIP_THE_END =
            GameRuleRegistry.register("skipTheEnd", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(true));

    @Override
    public void onInitialize() {}
}
