package com.xfw.meowmere;

import com.xfw.meowmere.entity.CatModel;
import com.xfw.meowmere.entity.CatRender;
import com.xfw.meowmere.entity.XEntityRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import static com.xfw.meowmere.entity.CatModel.LAYER_LOCATION;

@EventBusSubscriber(modid = Meowmere.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    private static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(XEntityRegistry.CATPROJECTILE.get(), CatRender::new);
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(LAYER_LOCATION, CatModel::createBodyLayer);
    }
}
