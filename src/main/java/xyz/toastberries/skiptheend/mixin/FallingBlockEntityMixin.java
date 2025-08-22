package xyz.toastberries.skiptheend.mixin;

import net.minecraft.entity.FallingBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static xyz.toastberries.skiptheend.TeleportContext.portalTravelingToEnd;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin {
    @ModifyVariable(method = "teleportTo", at = @At("STORE"))
    private boolean forceDuplicationBehaviour(boolean bl) {
        return portalTravelingToEnd.get() || bl;
    }
}

