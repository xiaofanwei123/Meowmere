package com.xfw.meowmere.entity;

import com.xfw.meowmere.Meowmere;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class CatRender extends EntityRenderer<CatProjectile> {
    private static final ResourceLocation TEXTURE = Meowmere.Resource("textures/entity/cat_projectile.png"); // 修正拼写
    private final CatModel<CatProjectile> model;

    public CatRender(EntityRendererProvider.Context context) {
        super(context);
        // 初始化模型
        this.model = new CatModel<>(context.bakeLayer(CatModel.LAYER_LOCATION));
    }

    @Override
    public void render(CatProjectile entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        // 调整位置和缩放
        poseStack.scale(1.0F, 1.0F, 1.0F);
        // 获取顶点构建器
        VertexConsumer vertexBuilder = buffer.getBuffer(this.model.renderType(TEXTURE));
        // 实际渲染模型
        this.model.renderToBuffer(poseStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY, 1);
        poseStack.popPose();
        super.render(entity, yaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(CatProjectile entity) {
        return TEXTURE;
    }
}