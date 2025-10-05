package com.kawodeda.farmandcharmfix.mixin;

import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.satisfy.farm_and_charm.core.item.food.EffectBlockItem;
import net.satisfy.farm_and_charm.core.registry.MobEffectRegistry;
import net.satisfy.farm_and_charm.core.registry.ObjectRegistry;
import net.satisfy.farm_and_charm.core.util.FarmAndCharmIdentifier;
import net.satisfy.farm_and_charm.core.util.GeneralUtil;
import net.satisfy.farm_and_charm.platform.PlatformHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

import static net.satisfy.farm_and_charm.core.registry.ObjectRegistry.*;

//@Mixin({ObjectRegistry.class})
public class ObjectRegistryMixin {
//    @Inject(method = "registerItem(Ljava/lang/String;Ljava/util/function/Supplier;)Ldev/architectury/registry/registries/RegistrySupplier;", at = @At("HEAD"), cancellable = true, remap = false)
    private static <T extends Item> void registerItem(String path, Supplier<T> itemSupplier, CallbackInfoReturnable<RegistrySupplier<T>> cir)
    {
//        FarmAndCharmFix.LOGGER.info("registerItem: " + path);
        RegistrySupplier<T> result = null;
        if (path.equals("farmers_bread"))
        {
            result = registerFarmersBlessingItem("farmers_bread", 30 * 20);
        }
        if (path.equals("farmers_breakfast"))
        {
            result = registerFarmersBlessingItem("farmers_breakfast", 60 * 20);
        }
        if (path.equals("oatmeal_with_strawberries"))
        {
            result = registerFarmersBlessingItem("oatmeal_with_strawberries", 10 * 20);
        }

        if (result != null)
        {
            cir.setReturnValue(result);
            cir.cancel();
        }
    }

    private static <T extends Item> RegistrySupplier<T> registerFarmersBlessingItem(String path, int duration)
    {
//        FarmAndCharmFix.LOGGER.info("overriding");
        Supplier<T> overriden = () -> (T)new EffectBlockItem(
                FARMERS_BREAD.get(),
                new Item.Properties().food(
                        new FoodProperties.Builder()
                                .nutrition(PlatformHelper.getNutrition(path))
                                .saturationMod(PlatformHelper.getSaturationMod(path))
                                .effect(new MobEffectInstance(MobEffectRegistry.FARMERS_BLESSING.get(), duration), 1.0f)
                                .build())
        );

        return GeneralUtil.registerItem(ITEMS, ITEM_REGISTRAR, new FarmAndCharmIdentifier(path), overriden);
    }
}
