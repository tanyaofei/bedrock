package io.github.hello09x.bedrock.util;

import com.google.common.base.Throwables;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;

public class Exceptions {

    @SuppressWarnings("unchecked")
    public static <T extends RuntimeException> T sneaky(@NotNull Throwable e) {
        return (T) e;
    }

    public static void noException(@NotNull Catchable runnable) {
        try {
            runnable.run();
        } catch (Throwable ignored) {

        }
    }

    public static void catchException(@NotNull Plugin plugin, @NotNull Catchable runnable) {
        try {
            runnable.run();
        } catch (Throwable e) {
            plugin.getLogger().severe(Throwables.getStackTraceAsString(e));
        }
    }

    @FunctionalInterface
    public interface Catchable {
        void run() throws Throwable;
    }

}
