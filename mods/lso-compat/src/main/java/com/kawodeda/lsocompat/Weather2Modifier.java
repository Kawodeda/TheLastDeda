package com.kawodeda.lsocompat;

import com.kawodeda.lsocompat.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import weather2.ClientTickHandler;
import weather2.ServerTickHandler;
import weather2.config.WeatherUtilConfig;
import weather2.util.WeatherUtilEntity;
import weather2.util.WindReader;
import weather2.weathersystem.WeatherManager;
import weather2.weathersystem.storm.StormObject;
import weather2.weathersystem.storm.WeatherObject;
import weather2.weathersystem.storm.WeatherObjectParticleStorm;

import java.util.Optional;

public class Weather2Modifier extends ModifierBase
{
    private static final Logger _logger = LogManager.getLogger();
    private static final float expectedMinWindSpeed = 0.0f;
    private static final float expectedMaxWindSpeed = 2.0f;

    public Weather2Modifier() { super(); }

    @Override
    public float getWorldInfluence(Player player, Level level, BlockPos pos)
    {
        var dimension = level.dimension().location().toString();
        if (!WeatherUtilConfig.listDimensionsWeather.contains(dimension))
        {
            _logger.debug("No weather in dimension: {}", dimension);
            return 0.0f;
        }
        _logger.debug("Player: {}", player);
        BlockPos lowerPos;
        BlockPos upperPos;
        if (player != null)
        {
            var boundingBox = player.getBoundingBox();
            lowerPos = new BlockPos(
                    (int) Math.floor(boundingBox.minX),
                    (int) Math.floor(boundingBox.minY),
                    (int) Math.floor(boundingBox.minZ)
            );
            upperPos = new BlockPos(
                    (int) Math.floor(boundingBox.maxX),
                    (int) Math.floor(boundingBox.maxY),
                    (int) Math.floor(boundingBox.maxZ)
            );
        }
        else
        {
            lowerPos = pos;
            upperPos = pos.above();
        }
        if (!WeatherUtilEntity.isPosOutside(level, lowerPos.getCenter(), false, false)
        && !WeatherUtilEntity.isPosOutside(level, upperPos.getCenter(), false, false))
        {
            _logger.debug("Player inside");
            return 0.0f;
        }

        var windSpeed = Mth.clamp(WindReader.getWindSpeed(level, upperPos), expectedMinWindSpeed, expectedMaxWindSpeed);
        var windAngle = WindReader.getWindAngle(level, upperPos.getCenter());
        var precipitation = isRainingAt(level, upperPos);

        _logger.debug("Wind speed: {}, Wind angle: {}, Precipitating: {}", windSpeed, windAngle, precipitation);

        var weatherBase = precipitation
                .map(value -> value == Biome.Precipitation.RAIN
                    ? Config.baseRainTemperature
                    : Config.baseSnowTemperature)
                .orElseGet(() -> Config.baseWindTemperature);

        var modifier = windSpeed;
        if (precipitation.isPresent())
        {
            modifier += 1;
        }

        return weatherBase.floatValue() * modifier;
    }

    private Optional<Biome.Precipitation> isRainingAt(Level level, BlockPos pos)
    {
        double maxStormDist = ((double) (512)) / 4 * 3;
        Vec3 plPos = new Vec3(pos.getX(), StormObject.static_YPos_layer0, pos.getZ());
        var weather = getWeatherManager(level);
        WeatherObject closestStorm = null;
        StormObject storm = weather.getClosestStorm(
                plPos,
                maxStormDist,
                StormObject.STATE_FORMING,
                -1,
                true);

        _logger.debug("Closest storm: {}", storm == null ? null : storm.pos);

        var snowstorm = weather.getClosestParticleStormByIntensity(plPos, WeatherObjectParticleStorm.StormType.SNOWSTORM);

        _logger.debug("Closest snowstorm: {}", snowstorm == null ? null : snowstorm.pos);

        if (storm == null)
        {
            closestStorm = snowstorm;
        }
        else if (snowstorm == null)
        {
            closestStorm = storm;
        }
        else
        {
            closestStorm = plPos.distanceTo(storm.pos) >= plPos.distanceTo(snowstorm.pos)
                    ? snowstorm
                    : storm;
        }

        _logger.debug("Closest storm: {}", closestStorm);

        if (closestStorm == null)
        {
            return Optional.empty();
        }

        float sizeToUse = closestStorm.getSize();
        double stormDist = closestStorm.pos.distanceTo(plPos);

        _logger.debug("sizeToUse: {}; stormDist: {}", sizeToUse, stormDist);

        if (!(sizeToUse > stormDist)) {
            _logger.debug("too far");
            return Optional.empty();
        } else {
            Biome biome = level.getBiome(pos).value();
            var biomePrecipitation = biome.getPrecipitationAt(pos);
            _logger.debug("Biome precipitation: {}", biomePrecipitation);
            return biomePrecipitation == Biome.Precipitation.RAIN
                    || biomePrecipitation == Biome.Precipitation.SNOW
                    ? Optional.of(biomePrecipitation)
                    : Optional.empty();
        }
    }

    private WeatherManager getWeatherManager(Level level)
    {
        return level.isClientSide
                ? getWeatherManagerClient()
                : ServerTickHandler.getWeatherManagerFor(level);
    }

    @OnlyIn(Dist.CLIENT)
    private static WeatherManager getWeatherManagerClient() {
        return ClientTickHandler.weatherManager;
    }
}
