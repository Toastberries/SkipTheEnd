package xyz.toastberries.skiptheend;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;


public class SkipTheEnd implements ModInitializer {
    public static final GameRule<Boolean> SKIP_THE_END =
            GameRuleBuilder.forBoolean(true)
                    .category(GameRuleCategory.MISC)
                    .buildAndRegister(Identifier.fromNamespaceAndPath("skip_the_end", "skip_the_end"));

    @Override
    public void onInitialize() {}
}
