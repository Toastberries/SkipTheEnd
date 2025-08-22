package xyz.toastberries.skiptheend.mixin;

import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static xyz.toastberries.skiptheend.TeleportContext.portalTravelingToEnd;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends EntityMixin {
    @Redirect(method = "teleportTo(Lnet/minecraft/world/TeleportTarget;)Lnet/minecraft/server/network/ServerPlayerEntity;",
            at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/server/world/ServerWorld;getRegistryKey()Lnet/minecraft/registry/RegistryKey;"))
    private RegistryKey<World> forceCrossDimensionalTeleportLogic(ServerWorld instance) {
        return portalTravelingToEnd.get() ? null : instance.getRegistryKey();
    }
}
