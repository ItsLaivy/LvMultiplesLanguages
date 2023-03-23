package codes.laivy.mlanguage.injection;

import io.netty.channel.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class InjectionUtils {

//    private static @NotNull Channel getPlayerChannel(@NotNull Player player) {
//        try {
//            PlayerConnection conn = ((CraftPlayer) player).getHandle().playerConnection;
//            return conn.a().channel;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    public static void removePlayer(@NotNull Player player) {
//        try {
//            Channel channel = getPlayerChannel(player);
//
//            channel.eventLoop().submit(() -> {
//                channel.pipeline().remove(player.getUniqueId().toString());
//            });
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

    public static void injectPlayer(@NotNull Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                super.channelRead(channelHandlerContext, packet);
            }

            @Override
            public void write(ChannelHandlerContext channelHandlerContext, Object packet, ChannelPromise channelPromise) throws Exception {
//                if (packet instanceof PacketPlayOutSetSlot) {
//                    PacketPlayOutSetSlot current = (PacketPlayOutSetSlot) packet;
//
//                    Field field = current.getClass().getDeclaredField("a");
//                    field.setAccessible(true);
//                    int a = field.getInt(current);
//
//                    field = current.getClass().getDeclaredField("b");
//                    field.setAccessible(true);
//                    int b = field.getInt(current);
//
//                    field = current.getClass().getDeclaredField("c");
//                    field.setAccessible(true);
//                    net.minecraft.server.v1_8_R3.ItemStack item = (net.minecraft.server.v1_8_R3.ItemStack) field.get(current);
//
//                    if (CraftItemStack.asBukkitCopy(item).getType() == Material.DIAMOND) {
//                        current = new PacketPlayOutSetSlot(a, b, CraftItemStack.asNMSCopy(new ItemStack(Material.REDSTONE_BLOCK, item.count)));
//                    }
//
//                    super.write(channelHandlerContext, current, channelPromise);
//                    return;
//                }

                super.write(channelHandlerContext, packet, channelPromise);
            }
        };

//        ChannelPipeline pipeline = getPlayerChannel(player).pipeline();
//        pipeline.addBefore("packet_handler", player.getUniqueId().toString(), channelDuplexHandler);
    }

}

