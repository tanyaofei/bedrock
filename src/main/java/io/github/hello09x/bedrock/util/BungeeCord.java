package io.github.hello09x.bedrock.util;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageRecipient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@SuppressWarnings("UnstableApiUsage")
public class BungeeCord {

    public static final String CHANNEL = "BungeeCord";

    public static boolean getServer(@NotNull Plugin plugin) {
        return sendPluginMessage(plugin, () -> {
            var out = ByteStreams.newDataOutput();
            out.writeUTF(SubChannel.GET_SERVER);
            return out.toByteArray();
        });
    }

    public static boolean getPlayerList(@NotNull Plugin plugin, @NotNull String server) {
        return sendPluginMessage(plugin, () -> {
            var out = ByteStreams.newDataOutput();
            out.writeUTF(SubChannel.PLAYER_LIST);
            out.writeUTF(server);
            return out.toByteArray();
        });
    }

    public static boolean connectOther(@NotNull Plugin plugin, @NotNull String playerName, @NotNull String server) {
        return sendPluginMessage(plugin, () -> {
            var out = ByteStreams.newDataOutput();
            out.writeUTF("ConnectOther");
            out.writeUTF(playerName);
            out.writeUTF(server);
            return out.toByteArray();
        });
    }

    public static boolean getPlayerList(@NotNull Plugin plugin) {
        return getPlayerList(plugin, "ALL");
    }

    public static boolean sendPluginMessage(@NotNull Plugin plugin, @NotNull Supplier<byte[]> message) {
        var r = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        if (r == null) {
            return false;
        }

        sendPluginMessage(plugin, r, message.get());
        return true;
    }

    public static void sendPluginMessage(@NotNull Plugin plugin, @NotNull PluginMessageRecipient recipient, byte @NotNull [] message) {
        recipient.sendPluginMessage(plugin, CHANNEL, message);
    }

    public static boolean sendForwardPluginMessage(@NotNull Plugin plugin, @NotNull String subchannel, @NotNull Supplier<byte[]> message) {
        return sendPluginMessage(plugin, () -> asForward(subchannel, message.get()));
    }

    public static boolean sendForwardPluginMessage(@NotNull Plugin plugin, @NotNull String subchannel, byte @NotNull [] message) {
        return sendPluginMessage(plugin, () -> asForward(subchannel, message));
    }

    public static byte @NotNull [] asForward(@NotNull String subchannel, @NotNull byte[] message) {
        var packet = ByteStreams.newDataOutput();
        packet.writeUTF("Forward");
        packet.writeUTF("ONLINE");
        packet.writeUTF(subchannel);

        packet.writeShort(message.length);
        packet.write(message);
        return packet.toByteArray();
    }


    public static byte @NotNull [] asReceivedForward(@NotNull String subchannel, byte @NotNull [] message) {
        var packet = ByteStreams.newDataOutput();
        packet.writeUTF(subchannel);
        packet.writeShort(message.length);
        packet.write(message);
        return packet.toByteArray();
    }

    /**
     * 解析经过 BungeeCord 转发的自定义插件消息, 用于解析远程消息
     *
     * @param subchannel 子频道
     * @param received   插件消息
     * @return 解析后的插件消息
     */
    public static @Nullable ByteArrayDataInput parseReceivedForward(@NotNull String subchannel, byte @NotNull [] received) {
        var in = ByteStreams.newDataInput(received);
        if (!subchannel.equals(in.readUTF())) {
            return null;
        }

        var length = in.readShort();
        byte[] message = new byte[length];
        in.readFully(message);
        return ByteStreams.newDataInput(message);
    }

    public interface SubChannel {
        String PLAYER_LIST = "PlayerList";

        String GET_SERVER = "GetServer";
    }


}
