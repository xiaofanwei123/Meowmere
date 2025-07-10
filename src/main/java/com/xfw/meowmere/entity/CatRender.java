package com.xfw.meowmere.entity;

import com.mojang.math.Axis;
import com.xfw.meowmere.Meowmere;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

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
        poseStack.translate(0.0F, 0.2F, 0.0F);
        Vec3 motion = entity.getDeltaMovement();
        float xRot = -((float) (Mth.atan2(motion.horizontalDistance(), motion.y) * (double) (180F / (float) Math.PI)) - 90.0F);
        float yRot = -((float) (Mth.atan2(motion.z, motion.x) * (double) (180F / (float) Math.PI)) + 90.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(xRot));

        // 获取顶点构建器
        VertexConsumer vertexBuilder = buffer.getBuffer(this.model.renderType(TEXTURE));
        // 实际渲染模型
        this.model.renderToBuffer(poseStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.render(entity, yaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(CatProjectile entity) {
        return TEXTURE;
    }
}