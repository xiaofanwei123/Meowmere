package com.xfw.meowmere.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class XEntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, "meowmere");

    public static final DeferredHolder<EntityType<?>, EntityType<CatProjectile>> CATPROJECTILE = ENTITIES.register("cat_projectile", () ->
            EntityType.Builder.<CatProjectile>of(CatProjectile::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .fireImmune()
                    .build("cat_projectile")
    );
}
