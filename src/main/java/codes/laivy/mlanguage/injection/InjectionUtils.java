package codes.laivy.mlanguage.injection;

import codes.laivy.mlanguage.api.item.ItemTranslator;
import codes.laivy.mlanguage.reflection.classes.packets.PacketPlayOutSetSlot;
import codes.laivy.mlanguage.reflection.classes.player.EntityPlayer;
import io.netty.channel.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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
                        //noinspection unchecked
                        @NotNull ItemTranslator<ItemStack, Player> translator = (ItemTranslator<ItemStack, Player>) Objects.requireNonNull(multiplesLanguagesBukkit().getApi().getItemTranslator());

                        // TODO: 24/03/2023 Um sistema para definir o nome do real NMS item para o local padrão da lingua (assim não teremos problemas com items diferentes)
                        
                        PacketPlayOutSetSlot current = new PacketPlayOutSetSlot(packet);
                        ItemStack item = current.getItemStack().getCraftItemStack().getItemStack();

                        if (translator.isTranslatable(item)) {
                            packet = translator.translate(item, player, current.getWindowId(), current.getSlot()).getValue();
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

