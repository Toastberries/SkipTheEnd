package xyz.toastberries.skiptheend.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

import static xyz.toastberries.skiptheend.TeleportContext.portalTravelingToEnd;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Redirect(method = "tickPortalTeleportation",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;teleportTo(Lnet/minecraft/world/TeleportTarget;)Lnet/minecraft/entity/Entity;"))
    private Entity redirectEndBoundPortalTravel(Entity instance, TeleportTarget teleportTarget) {
        if (teleportTarget.world().getRegistryKey() == World.END && instance.getWorld().getRegistryKey() != World.END) {
            portalTravelingToEnd.set(true);
            TeleportTarget returnPortalTeleportTarget = ((Entity)(Object) this).portalManager.createTeleportTarget(teleportTarget.world(), instance);
            Entity entity = instance.teleportTo(returnPortalTeleportTarget);
            portalTravelingToEnd.set(false);
            return entity;
        }
        return instance.teleportTo(teleportTarget);
    }

    @ModifyVariable(method = "teleportTo", at = @At("STORE"))
    private boolean forceCrossDimensionalTeleportLogic(boolean bl) {
        return portalTravelingToEnd.get() || bl;
    }

    @Redirect(method = "teleportSpectatingPlayers",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;getPlayers()Ljava/util/List;"))
    private List<ServerPlayerEntity> removeSelfFromTeleportedSpectators(ServerWorld instance) {
        return portalTravelingToEnd.get()
                ? instance.getPlayers().stream().filter(player -> player != (Object) this).toList()
                : instance.getPlayers();
    }
}
