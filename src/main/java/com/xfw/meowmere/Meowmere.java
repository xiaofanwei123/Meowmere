package com.xfw.meowmere;

import com.mojang.logging.LogUtils;
import com.xfw.meowmere.entity.CatProjectile;
import com.xfw.meowmere.item.MeowmereSword;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.SimpleTier;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

@Mod(Meowmere.MODID)
public class Meowmere {
    public static final String MODID = "meowmere";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final RandomSource RANDOM = RandomSource.create();

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems("meowmere");
    public static final DeferredItem<SwordItem> MEOWMERE
            = ITEMS.register("meowmere", ()-> new MeowmereSword(
            new SimpleTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2000, -4f, -1f, 10, () -> Ingredient.of(Items.ECHO_SHARD))));
    public static final DeferredItem<SwordItem> CAT_SWORD
            = ITEMS.register("cat_sword", ()-> new SwordItem(
            new SimpleTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2000, -4f, -1f, 10, () -> Ingredient.of(Items.ECHO_SHARD)),new Item.Properties().fireResistant().durability(2000)));

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, "meowmere");
    public static final DeferredHolder<EntityType<?>, EntityType<CatProjectile>> CATPROJECTILE = ENTITIES.register("cat_projectile", () ->
            EntityType.Builder.<CatProjectile>of(CatProjectile::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(64)
                    .fireImmune()
                    .build("cat_projectile")
    );

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPE = DeferredRegister.create(Registries.PARTICLE_TYPE, Meowmere.MODID);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> RAINBOW = PARTICLE_TYPE.register("rainbow", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> PLAYER_RAINBOW = PARTICLE_TYPE.register("player_rainbow", () -> new SimpleParticleType(false));

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if ( event.getTabKey() == CreativeModeTabs.COMBAT ) {
            event.accept(MEOWMERE);
        }
    }

    public Meowmere(IEventBus modEventBus)
    {
        ITEMS.register(modEventBus);
        ENTITIES.register(modEventBus);
        PARTICLE_TYPE.register(modEventBus);
        modEventBus.addListener(this::addCreative);
    }

    public static ResourceLocation Resource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

}
