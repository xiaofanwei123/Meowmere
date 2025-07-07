package com.xfw.meowmere;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber
public class ServerPlayerEvents {
    //左键事件,武器左键
    @SubscribeEvent
    public static void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.getLevel().isClientSide) {
            PacketDistributor.sendToServer(new MessageSwingArm());
        }
    }
}
