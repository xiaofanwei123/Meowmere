package com.xfw.meowmere.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class XItemRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems("meowmere");

    //meowmere
    public static final DeferredItem<SwordItem> MEOWMERE
            = ITEMS.register("meowmere", ()-> new MeowmereSword(
            new SimpleTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2000, -4f, -1f, 10, () -> Ingredient.of(Items.ECHO_SHARD))));
}
