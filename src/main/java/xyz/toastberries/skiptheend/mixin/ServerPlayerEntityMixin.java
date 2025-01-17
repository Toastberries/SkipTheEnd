package xyz.toastberries.skiptheend.mixin;

import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import xyz.toastberries.skiptheend.PortalTraveler;
import xyz.toastberries.skiptheend.EndPortalHelper;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @ModifyVariable(method = "teleportTo(Lnet/minecraft/world/TeleportTarget;)Lnet/minecraft/server/network/ServerPlayerEntity;", at = @At(value = "HEAD"), argsOnly = true)
    private TeleportTarget modifyTeleportTarget(TeleportTarget teleportTarget) {
        final ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        return (((PortalTraveler) player).skipTheEnd$isPortalTravelingToEnd())
                ? EndPortalHelper.createEndPortalTeleportTarget(teleportTarget.world(), player)
                : teleportTarget;
    }

    @Redirect(method = "teleportTo(Lnet/minecraft/world/TeleportTarget;)Lnet/minecraft/server/network/ServerPlayerEntity;",
            at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/server/world/ServerWorld;getRegistryKey()Lnet/minecraft/registry/RegistryKey;"))
    public RegistryKey<World> redirectGetRegistryKey(ServerWorld instance) {
        final ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        return ((PortalTraveler) player).skipTheEnd$isPortalTravelingToEnd()
                // A null here forces the if statement to be false,
                // which makes the game act like the player is switching worlds, even when they aren't.
                ? null
                : instance.getRegistryKey();
    }

}
