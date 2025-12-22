package xyz.toastberries.skiptheend.mixin;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static xyz.toastberries.skiptheend.TeleportContext.portalTravelingToEnd;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerEntityMixin extends EntityMixin {
    @Redirect(method = "teleport(Lnet/minecraft/world/level/portal/TeleportTransition;)Lnet/minecraft/server/level/ServerPlayer;",
            at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/server/level/ServerLevel;dimension()Lnet/minecraft/resources/ResourceKey;"))
    private ResourceKey<Level> forceCrossDimensionalTeleportLogic(ServerLevel instance) {
        return portalTravelingToEnd.get() ? null : instance.dimension();
    }
}
