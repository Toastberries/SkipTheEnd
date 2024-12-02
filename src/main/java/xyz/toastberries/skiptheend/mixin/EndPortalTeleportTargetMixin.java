package xyz.toastberries.skiptheend.mixin;

import net.minecraft.block.EndPortalBlock;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EndPortalBlock.class)
public class EndPortalTeleportTargetMixin {
    @ModifyVariable(method = "createTeleportTarget", at = @At("STORE"), ordinal = 0)
    private RegistryKey<World> modifyDestinationRegistryKey(RegistryKey<World> registryKey) {
        return World.OVERWORLD;
    }
}