package com.xfw.meowmere.item;

import com.xfw.meowmere.entity.CatProjectile;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;


@EventBusSubscriber(modid = "meowmere")
public class MeowmereSword extends SwordItem {

    public MeowmereSword(Tier tier) {
        super(tier, new Item.Properties().fireResistant().durability(tier.getUses())
                .component(DataComponents.ATTRIBUTE_MODIFIERS,
                        createAttributes(tier,10, -2.4F))
        );
    }

    //玩家攻击后的事件
    @SubscribeEvent
    public static void onPlayerAttack(LivingDamageEvent.Pre event){

    }

    public static void onLeftClick(Player player,double damage) {
        CatProjectile catProjectile = new CatProjectile(player.level(), player);
        catProjectile.setPos(player.position().add(0.0, (double)player.getEyeHeight() - catProjectile.getBoundingBox().getYsize() * 0.5, 0.0));
        catProjectile.shoot(player.getLookAngle());
        catProjectile.setDamage((float) damage);
        player.level().addFreshEntity(catProjectile);
    }
}
