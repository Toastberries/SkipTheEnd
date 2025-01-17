package xyz.toastberries.skiptheend.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.toastberries.skiptheend.PortalTraveler;
import xyz.toastberries.skiptheend.EndPortalHelper;

@Mixin(Entity.class)
public class EntityMixin implements PortalTraveler {

    /// Used to identify end-bound portal travel, filters out things like command & ender pearl teleports.
    @Unique
    private boolean portalTravelingToEnd = false;

    @Override
    public boolean skipTheEnd$isPortalTravelingToEnd() {
        return portalTravelingToEnd;
    }

    @Inject(method = "tickPortalTeleportation", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;teleportTo(Lnet/minecraft/world/TeleportTarget;)Lnet/minecraft/entity/Entity;"))
    private void setFlagPreTeleport(CallbackInfo ci, @Local TeleportTarget teleportTarget) {
        final Entity entity = (Entity) (Object) this;
        portalTravelingToEnd = entity.getWorld().getRegistryKey() != World.END && teleportTarget.world().getRegistryKey() == World.END;
    }

    @Inject(method = "tickPortalTeleportation", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/Entity;teleportTo(Lnet/minecraft/world/TeleportTarget;)Lnet/minecraft/entity/Entity;"))
    private void resetFlagPostTeleport(CallbackInfo ci) {
        portalTravelingToEnd = false;
    }


    @Redirect(method = "teleportTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;teleportCrossDimension(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/TeleportTarget;)Lnet/minecraft/entity/Entity;"))
    private Entity redirectCrossDimension(Entity instance, ServerWorld destinationWorld, TeleportTarget teleportTarget) {
        if (skipTheEnd$isPortalTravelingToEnd()) {
            TeleportTarget endPortalTeleportTarget = EndPortalHelper.createEndPortalTeleportTarget(destinationWorld, instance);
            return instance.teleportCrossDimension(endPortalTeleportTarget.world(), endPortalTeleportTarget);
        }
        return instance.teleportCrossDimension(destinationWorld, teleportTarget);
    }

}
