package codes.laivy.mlanguage.injection;

import codes.laivy.mlanguage.api.bukkit.translator.BukkitItemTranslator;
import codes.laivy.mlanguage.reflection.classes.packets.PacketPlayOutSetSlot;
import codes.laivy.mlanguage.reflection.classes.player.EntityPlayer;
import codes.laivy.mlanguage.reflection.versions.V1_18_R1;
import codes.laivy.mlanguage.reflection.versions.V1_8_R1;
import codes.laivy.mlanguage.utils.ReflectionUtils;
import io.netty.channel.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static codes.laivy.mlanguage.main.BukkitMultiplesLanguages.multiplesLanguagesBukkit;

public class InjectionUtils {

    private static @NotNull Channel getPlayerChannel(@NotNull Player player) {
        return EntityPlayer.getEntityPlayer(player).getConnection().getNetworkManager().getChannel();
    }

    public static void removePlayer(@NotNull Player player) {
        try {
            Channel channel = getPlayerChannel(player);

            channel.eventLoop().submit(() -> {
                channel.pipeline().remove(player.getUniqueId().toString());
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void injectPlayer(@NotNull Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                super.channelRead(channelHandlerContext, packet);
            }

            @Override
            public void write(ChannelHandlerContext channelHandlerContext, Object packet, ChannelPromise channelPromise) throws Exception {
                try {
                    if (packet.getClass().equals(multiplesLanguagesBukkit().getVersion().getClassExec("PacketPlayOutSetSlot").getReflectionClass())) {
                        PacketPlayOutSetSlot current = new PacketPlayOutSetSlot(packet);

                        if (current.getItemStack().getValue() != null && current.getItemStack().getTag() != null) {
                            BukkitItemTranslator translator = multiplesLanguagesBukkit().getApi().getItemTranslator();
                            ItemStack item = current.getItemStack().getCraftItemStack().getItemStack().clone();

                            int state;
                            if (ReflectionUtils.isCompatible(V1_18_R1.class)) {
                                state = current.getState();
                            } else {
                                state = -1;
                            }

                            if (translator.isTranslatable(item)) {
                                packet = translator.translate(item, player, current.getWindowId(), current.getSlot(), state).getValue();
                            }
                        }
                    }

                    super.write(channelHandlerContext, packet, channelPromise);
                } catch (Throwable e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        };

        ChannelPipeline pipeline = getPlayerChannel(player).pipeline();
        pipeline.addBefore("packet_handler", player.getUniqueId().toString(), channelDuplexHandler);
    }

}

