package io.github.hello09x.bedrock.util;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PluginMessages {

    /**
     * 转成 BungeeCord 要求的转发自定义插件消息的格式, 用于发送
     *
     * @param subchannel 自定义子频道
     * @param out        自定义消息
     * @return 消息字节数组
     */
    public static byte @NotNull [] box(@NotNull String subchannel, @NotNull ByteArrayDataOutput out) {
        var data = out.toByteArray();
        var box = ByteStreams.newDataOutput();
        box.writeUTF("Forward");
        box.writeUTF("ONLINE");
        box.writeUTF(subchannel);

        box.writeShort(data.length);
        box.write(data);
        return box.toByteArray();
    }

    /**
     * 转成 BungeeCord 转发后的自定义插件消息的格式, 用于解析本地消息
     * <p>这个方法存在的意义是用来保证与接收到的远程消息格式一致, 方便在不经过 BungeeCord 转发也能让 {@link #unbox(String, byte[])} 使用</p>
     *
     * @param subchannel 自定义子频道
     * @param out        自定义消息
     * @return 消息字节数组
     */
    public static byte @NotNull [] boxLocal(@NotNull String subchannel, @NotNull ByteArrayDataOutput out) {
        var data = out.toByteArray();

        var wrap = ByteStreams.newDataOutput();
        wrap.writeUTF(subchannel);
        wrap.writeShort(data.length);
        wrap.write(data);
        return wrap.toByteArray();
    }

    /**
     * 解析经过 BungeeCord 转发的自定义插件消息, 用于解析远程消息
     *
     * @param subchannel 子频道
     * @param message    插件消息
     * @return 解析后的插件消息
     */
    public static @Nullable ByteArrayDataInput unbox(@NotNull String subchannel, byte @NotNull [] message) {
        var in = ByteStreams.newDataInput(message);
        if (!subchannel.equals(in.readUTF())) {
            return null;
        }

        var length = in.readShort();
        byte[] data = new byte[length];
        in.readFully(data);
        return ByteStreams.newDataInput(data);
    }

}
