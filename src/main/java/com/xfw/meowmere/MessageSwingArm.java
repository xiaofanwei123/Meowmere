package com.xfw.meowmere;

import com.xfw.meowmere.item.MeowmereSword;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

//武器挥动需要客户端向服务端发送
public record MessageSwingArm() implements CustomPacketPayload {

    public static final Type<MessageSwingArm> TYPE = new Type<>(Meowmere.Resource("message_swing_arm"));
    public static final StreamCodec<ByteBuf, MessageSwingArm> STREAM_CODEC = CustomPacketPayload.codec(
            MessageSwingArm::encode,
            MessageSwingArm::decode
    );

    private void encode(ByteBuf byteBuf) {}

    private static MessageSwingArm decode(ByteBuf byteBuf) {
        return new MessageSwingArm();
    }

    @Override
    public Type<MessageSwingArm> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer){
                if (serverPlayer.getMainHandItem().getItem() == Meowmere.MEOWMERE.get() && serverPlayer.swingTime <= 0) {
                    MeowmereSword.addProjectile(serverPlayer);
                }
            }}).exceptionally(e -> {
            context.disconnect(Component.translatable("neoforge.network.invalid_flow", e.getMessage()));
            return null;
        });
    }
}
