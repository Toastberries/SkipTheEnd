package xyz.toastberries.skiptheend.mixin;

import net.minecraft.world.entity.item.FallingBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static xyz.toastberries.skiptheend.TeleportContext.portalTravelingToEnd;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin {
    @ModifyVariable(method = "teleport", at = @At("STORE"), name = "fromOrToEnd")
    private boolean forceDuplicationBehaviour(boolean fromOrToEnd) {
        return portalTravelingToEnd.get() || fromOrToEnd;
    }
}

