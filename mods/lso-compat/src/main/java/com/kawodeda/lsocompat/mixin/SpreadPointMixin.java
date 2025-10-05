package com.kawodeda.lsocompat.mixin;

import com.kawodeda.lsocompat.LsoCompat;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.mod.common.world.RaycastUtilsKt;
import sfiomn.legendarysurvivaloverhaul.util.SpreadPoint;

@Mixin({SpreadPoint.class})
public class SpreadPointMixin {

    @Shadow @Final
    private BlockPos pos;

    @Shadow @Final
    private Level level;

    @Inject(method = "setCanSeeSky", at = @At("HEAD"), remap = false)
    private void checkShipAbove(CallbackInfo ci)
    {
        Vec3 start = pos.getCenter();
        Vec3 end = start.add(0, 30, 0);

        var hitResult = RaycastUtilsKt.clipIncludeShips(level, new ClipContext(
           start,
           end,
           ClipContext.Block.COLLIDER,
           ClipContext.Fluid.NONE,
           null
        ));

        log("Hit type: " + hitResult.getType());
        if (hitResult.getType() != HitResult.Type.MISS)
        {
            log("Hit pos: " + hitResult.getLocation());
        }
    }

    private void log(String msg)
    {
        LsoCompat.LOGGER.info(msg);
    }
}
