package codes.laivy.mlanguage.api.bukkit.natives;

import codes.laivy.mlanguage.api.bukkit.reflection.classes.packets.PacketPlayOutWindowItems;
import codes.laivy.mlanguage.api.bukkit.translator.BukkitItemTranslator;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.packets.PacketPlayOutSetSlot;
import codes.laivy.mlanguage.api.bukkit.reflection.classes.player.EntityPlayer;
import codes.laivy.mlanguage.api.bukkit.reflection.versions.V1_17_R1;
import codes.laivy.mlanguage.utils.ReflectionUtils;
import io.netty.channel.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static codes.laivy.mlanguage.api.bukkit.BukkitMultiplesLanguagesAPI.getDefApi;

/**
 * The injection of the default api of LvMultiplesLanguages
 */
public class InjectionManager {

    private final @NotNull BukkitItemTranslator translator;

    public InjectionManager(@NotNull BukkitItemTranslator translator) {
        this.translator = translator;
    }

    public @NotNull BukkitItemTranslator getTranslator() {
        return translator;
    }

    private static @NotNull Channel getPlayerChannel(@NotNull Player player) {
        return EntityPlayer.getEntityPlayer(player).getConnection().getNetworkManager().getChannel();
    }

    public void remove(@NotNull Player player) {
        try {
            Channel channel = getPlayerChannel(player);

            channel.eventLoop().submit(() -> {
                channel.pipeline().remove(player.getUniqueId().toString());
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void inject(@NotNull Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                super.channelRead(channelHandlerContext, packet);
            }

            @Override
            public void write(ChannelHandlerContext channelHandlerContext, Object packet, ChannelPromise channelPromise) throws Exception {
                try {
                    if (packet.getClass().equals(getDefApi().getVersion().getClassExec("PacketPlayOutSetSlot").getReflectionClass())) {
                        PacketPlayOutSetSlot current = new PacketPlayOutSetSlot(packet);

                        if (current.getItemStack().getValue() != null && current.getItemStack().getTag() != null) {
                            ItemStack item = current.getItemStack().getCraftItemStack().getItemStack().clone();

                            int state = -1;
                            if (ReflectionUtils.isCompatible(V1_17_R1.class)) {
                                V1_17_R1 v = (V1_17_R1) getDefApi().getVersion();

                                if (v.isStateEnabled()) {
                                    state = current.getStateId();
                                }
                            }

                            if (getTranslator().isTranslatable(item)) {
                                packet = getTranslator().translate(item, player, current.getWindowId(), current.getSlot(), state).getValue();
                            }
                        }
                    } else if (packet.getClass().equals(getDefApi().getVersion().getClassExec("PacketPlayOutWindowItems").getReflectionClass())) {
                        packet = getDefApi().getVersion().translateWindowItems(new PacketPlayOutWindowItems(packet), player).getValue();
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

