package com.xfw.meowmere.item;

import com.xfw.meowmere.Meowmere;
import com.xfw.meowmere.entity.CatProjectile;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.ai.attributes.Attributes;
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
                        createAttributes(tier,10, -2F))
        );
    }

    //玩家攻击后的事件
    @SubscribeEvent
    public static void onPlayerAttack(LivingDamageEvent.Pre event){
        if (event.getSource().getEntity() instanceof Player player
                && player.swingTime <= 0
                && player.getMainHandItem().getItem() == Meowmere.MEOWMERE.get()
                && event.getSource().getDirectEntity() instanceof Player) {
            addProjectile(player);
        }
    }

    public static void addProjectile(Player player) {
        float damage= (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
        CatProjectile catProjectile = new CatProjectile(player.level(), player);
        catProjectile.setPos(player.getLookAngle().normalize().add(player.getEyePosition()));
        catProjectile.shoot(player.getLookAngle(),1F);
        catProjectile.setDamage(1.25F * damage);
        player.level().addFreshEntity(catProjectile);
    }

}
