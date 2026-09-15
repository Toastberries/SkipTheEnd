package xyz.toastberries.skiptheend.mixin;


import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

import static xyz.toastberries.skiptheend.SkipTheEnd.SKIP_THE_END;
import static xyz.toastberries.skiptheend.TeleportContext.portalTravelingToEnd;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Redirect(method = "teleportToPortalDestination",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;teleport(Lnet/minecraft/world/level/portal/TeleportTransition;)Lnet/minecraft/world/entity/Entity;"))
    private Entity redirectEndBoundPortalTravel(Entity instance, TeleportTransition transition) {
        boolean gameRule = ((ServerLevel) instance.level()).getGameRules().get(SKIP_THE_END);
        if (transition.newLevel().dimension() == Level.END && instance.level().dimension() != Level.END && gameRule) {
            portalTravelingToEnd.set(true);
            TeleportTransition returnPortalTeleportTarget = ((Entity)(Object) this).portalProcess.getPortalDestination(transition.newLevel(), instance);
            Entity entity = instance.teleport(returnPortalTeleportTarget);
            portalTravelingToEnd.set(false);
            return entity;
        }
        return instance.teleport(transition);
    }

    @ModifyVariable(method = "teleport", at = @At("STORE"))
    private boolean forceCrossDimensionalTeleportLogic(boolean bl) {
        return portalTravelingToEnd.get() || bl;
    }

    @Redirect(method = "teleportSpectators",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;players()Ljava/util/List;"))
    private List<ServerPlayer> removeSelfFromTeleportedSpectators(ServerLevel instance) {
        return portalTravelingToEnd.get()
                ? instance.players().stream().filter(player -> player != (Object) this).toList()
                : instance.players();
    }
}
