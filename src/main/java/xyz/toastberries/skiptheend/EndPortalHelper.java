package xyz.toastberries.skiptheend;

import net.minecraft.block.Blocks;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.TeleportTarget;

public class EndPortalHelper {

    private static final EndPortalBlock END_PORTAL_BLOCK = (EndPortalBlock) Blocks.END_PORTAL;

    public static TeleportTarget createEndPortalTeleportTarget(ServerWorld serverWorld, Entity entity) {
        return END_PORTAL_BLOCK.createTeleportTarget(serverWorld, entity, entity.getBlockPos());
    }

}
