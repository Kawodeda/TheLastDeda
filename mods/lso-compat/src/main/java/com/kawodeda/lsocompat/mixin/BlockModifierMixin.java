package com.kawodeda.lsocompat.mixin;

import com.kawodeda.lsocompat.LsoCompat;
import com.kawodeda.lsocompat.config.Config;
import com.kawodeda.lsocompat.config.JsonConfig;
import com.kawodeda.lsocompat.config.JsonCreateFluidTemperature;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.world.RaycastUtilsKt;
import sfiomn.legendarysurvivaloverhaul.common.temperature.BlockModifier;
import sfiomn.legendarysurvivaloverhaul.util.SpreadPoint;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

@Mixin({BlockModifier.class})
public class BlockModifierMixin {

    @Inject(method = "doBlocksAndFluidsRoutine", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD, remap = false)
    private void doBlocksAndFluidsRoutine(
            Level level,
            BlockPos pos,
            CallbackInfo ci,
            HashSet<BlockPos> visitedBlockPos,
            ArrayList<SpreadPoint> visitedSpreadPoints)
    {
        LsoCompat.LOGGER.info("### visited spread points: {}", visitedSpreadPoints.size());

        var ship = VSGameUtilsKt.getShipManagingPos(level, pos);

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

    @Inject(method = "getTemperatureFromSpreadPoint", at = @At("HEAD"), cancellable = true, remap = false)
    private void getTemperatureFromSpreadPoint(Level level, SpreadPoint spreadPoint, CallbackInfoReturnable<Float> cir)
    {
        var blockEntity = level.getBlockEntity(spreadPoint.position());
        if (blockEntity == null)
        {
            return;
        }

        var logger = LogManager.getLogger();
        if (blockEntity instanceof SmartBlockEntity smartBlockEntity)
        {
            logger.debug("Smart block entity: {}", smartBlockEntity);

            var pipeBehaviour = getPipeBehaviour(smartBlockEntity);
            if (pipeBehaviour != null && pipeBehaviour.interfaces != null)
            {
                cir.setReturnValue(getPipeTemperature(pipeBehaviour, logger));
                return;
            }

            var fluidContainerTemp = getFluidContainerTemperature(smartBlockEntity, logger);
            if (fluidContainerTemp.isPresent())
            {
                cir.setReturnValue(fluidContainerTemp.get());
                return;
            }

            if (blockEntity instanceof FluidTankBlockEntity fluidTankBlockEntity)
            {
                var boilerTemp = calculateBoilerTemperature(fluidTankBlockEntity, logger);
                if (boilerTemp.isPresent())
                {
                    logger.debug("Boiler temp: {}", boilerTemp.get());
                    cir.setReturnValue(boilerTemp.get());
                    return;
                }

                fluidContainerTemp = getFluidContainerTemperature(fluidTankBlockEntity, logger);
                if (fluidContainerTemp.isPresent())
                {
                    cir.setReturnValue(fluidContainerTemp.get());
                    return;
                }

                logger.debug("Could not get temperature from fluid tank");
                cir.setReturnValue(0.0f);
                return;
            }
            if (blockEntity instanceof SteamEngineBlockEntity steamEngineBlockEntity)
            {
                var tank = steamEngineBlockEntity.getTank();
                if (tank == null)
                {
                    logger.debug("Could not get steam engine's tank");
                    cir.setReturnValue(0f);
                    return;
                }

                var temp = calculateBoilerTemperature(tank, logger);
                if (temp.isPresent())
                {
                    logger.debug("Steam engine temp: {}", temp.get());
                    cir.setReturnValue(temp.get());
                }
                else
                {
                    logger.debug("Could not get steam engine's temperature");
                    cir.setReturnValue(0f);
                }

                return;
            }
        }
    }

    private FluidTransportBehaviour getPipeBehaviour(SmartBlockEntity smartBlockEntity)
    {
        return smartBlockEntity.getBehaviour(FluidTransportBehaviour.TYPE);
    }

    private Optional<IFluidHandler> getFluidHandler(SmartBlockEntity smartBlockEntity)
    {
        return  smartBlockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve();
    }

    private Optional<Float> getFluidContainerTemperature(SmartBlockEntity smartBlockEntity, Logger logger)
    {
        var capability = getFluidHandler(smartBlockEntity);
        if (capability.isPresent())
        {
            var fluid = getFluid(capability.get());
            if (fluid != null)
            {
                var tankTemp = getFluidContainerTemperature(fluid);
                logger.debug("Fluid tank temp: {}", tankTemp);
                return Optional.of(tankTemp);
            }
            else
            {
                logger.debug("Could not get fluid from tank fluid capability");
                return Optional.empty();
            }
        }

        return Optional.empty();
    }

    private float getPipeTemperature(FluidTransportBehaviour pipeBehaviour, Logger logger)
    {
        logger.debug("Pipe or pump");

        for (Direction direction : pipeBehaviour.interfaces.keySet())
        {
            FluidStack fluidStack = pipeBehaviour.getProvidedOutwardFluid(direction);
            var fluidConf = getFluidConfig(fluidStack);
            if (fluidConf.isEmpty())
            {
                continue;
            }

            var fluid = fluidStack.getFluid().getFluidType().toString();
            var connection = pipeBehaviour.getConnection(direction);
            var pressure = connection != null
                    ? connection.getPressure().getSecond()
                    : 0f;

            logger.debug("Fluid stack: {}, Temp: {}, Amount: {}", fluid, fluidStack.getFluid().getFluidType().getTemperature(), fluidStack.getAmount());
            logger.debug("Pressure: {}", pressure);

            var temp = calucatePipeTemperature(fluidConf.get(), pressure);

            logger.debug("Temp: {}", temp);

            return  temp;
        }

        return  0.0f;
    }

    private float calucatePipeTemperature(JsonCreateFluidTemperature fluidConfig, float pressure)
    {
        var maxTempPressure = Config.maxPipeTemperaturePressure;
        var minTempPressure = Config.minPipeTemperaturePressure;
        if (pressure >= maxTempPressure)
        {
            return fluidConfig.flowingTemperature;
        }
        else if (pressure <= minTempPressure)
        {
            return fluidConfig.inContainerTemperature;
        }

        var slope = (fluidConfig.flowingTemperature - fluidConfig.inContainerTemperature) / (maxTempPressure - minTempPressure);
        var offset = fluidConfig.flowingTemperature - slope * maxTempPressure;

        return slope * pressure + offset;
    }

    private Optional<Float> calculateBoilerTemperature(FluidTankBlockEntity fluidTankBlockEntity, Logger logger)
    {
        var controller = fluidTankBlockEntity.getControllerBE();
        if (controller == null)
        {
            return  Optional.empty();
        }

        logger.debug("Boiler. Water supply: {}, Heat: {}", controller.boiler.waterSupply, controller.boiler.activeHeat);
//        logger.info("Theoretical heat level: {}. Heat level for water supply: {}", controller.boiler.getTheoreticalHeatLevel(), controller.boiler.getMaxHeatLevelForWaterSupply());

        var actualHeatLevel = Math.min(controller.boiler.getMaxHeatLevelForWaterSupply(), controller.boiler.activeHeat);

        logger.debug("Heat level: {}", actualHeatLevel);

        var boilerTemp = Config.boilerTemperatureModifier * actualHeatLevel;

//        logger.info("Boiler temp: {}", boilerTemp);

        return Optional.of(boilerTemp);
    }

    private float getFluidContainerTemperature(FluidStack fluidStack)
    {
        var fluidConf = getFluidConfig(fluidStack);
        if (fluidConf.isEmpty())
        {
            return  0.0f;
        }

        return fluidConf.get().inContainerTemperature;
    }

    private Optional<JsonCreateFluidTemperature> getFluidConfig(FluidStack fluidStack)
    {
        if (fluidStack.isEmpty())
            return Optional.empty();

        var fluid = fluidStack.getFluid().getFluidType().toString();
        var config = JsonConfig.createFluidTemperature.getOrDefault(fluid, null);
        if (config == null)
        {
            return Optional.empty();
        }

        return  Optional.of(config);
    }

    private FluidStack getFluid(IFluidHandler fluidHandler)
    {
        for (int i = 0; i < fluidHandler.getTanks(); i++) {
            FluidStack tank = fluidHandler.getFluidInTank(i);
            if(!tank.isEmpty()) return tank;
        }

        return null;
    }
}
