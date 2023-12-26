package io.github.hello09x.bedrock.util;

import org.bukkit.Bukkit;

public abstract class MCUtils {

    private final static boolean IS_FOLIA = Bukkit.getServer().getName().equals("Folia");

    public static void ensureMain() {
        if (!Bukkit.isPrimaryThread()) {
            throw new IllegalStateException("Must run in main thread");
        }
    }

    public static boolean isFolia() {
        return IS_FOLIA;
    }

}
